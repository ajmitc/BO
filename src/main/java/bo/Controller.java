package bo;

import bo.game.*;
import bo.game.conspirator.ConspiratorCard;
import bo.game.conspirator.ConspiratorCardPlayTiming;
import bo.game.conspirator.ConspiratorCardType;
import bo.game.conspirator.ConspiratorEffectResolver;
import bo.game.event.CurrentEventEffect;
import bo.game.event.EventCard;
import bo.game.event.EventCardType;
import bo.game.event.EventResolver;
import bo.game.interrogation.InterrogationCard;
import bo.game.interrogation.InterrogationEffect;
import bo.game.interrogation.InterrogationEffectResolver;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.location.LocationModifier;
import bo.game.location.LocationName;
import bo.game.player.*;
import bo.game.plot.Plot;
import bo.game.util.DieResult;
import bo.game.util.DissentReward;
import bo.game.util.Util;
import bo.view.View;
import bo.view.util.ViewUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private Model model;
    private View view;

    private EventResolver eventResolver;
    private ConspiratorEffectResolver conspiratorEffectResolver;
    private InterrogationEffectResolver interrogationEffectResolver;

    private int currentPlayerActionsAllowed = 3;
    private int currentPlayerActionsTaken = 0;
    private boolean currentPlayerConspireActionTaken = false;

    private boolean conspireActionDisabled = false;
    private boolean specialAbilityActionDisabled = false;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
        this.eventResolver = new EventResolver(model, view);
        this.conspiratorEffectResolver = new ConspiratorEffectResolver(model, view);
        this.interrogationEffectResolver = new InterrogationEffectResolver(model, view);

        view.getMainMenuPanel().getBtnNewGame().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Game game = new Game();
                model.setGame(game);

                // Allow user to set difficulty
                Difficulty difficulty = (Difficulty) ViewUtil.popupDropdown("Setup", "Select Difficulty", Difficulty.values());
                game.setDifficulty(difficulty);

                // TODO Allow user to set player roles
                game.getPlayers().add(new Player(Player.BECK));
                game.getPlayers().add(new Player(Player.OSTER));

                // Add the player board panels
                for (Player player: game.getPlayers()){
                    view.getGamePanel().getPlayerBoardsPanel().addPlayerBoard(player);
                    game.getBoard().getLocation(LocationName.TRAIN_STATION).getPlayers().add(player);
                }

                game.setNextPlayer();

                view.showGame();
                view.refresh();
            }
        });

        /*
CONSPIRE
You can only take the Conspire action once during your turn.
Take up to 3 dice, spending one action for each die taken.
Roll all of those dice and resolve the results in this order:
- For each eagle rolled , you and all other conspirators in your space raise suspicion by 1.
- For each target rolled, place that die on the Dissent Track.
- Total all the number results and gain that many actions this turn, plus any unspent actions (if any) that you didn't use for the Conspire action.
         */
        view.getGamePanel().getActionPanel().getBtnConspire().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (conspireActionDisabled){
                    ViewUtil.popupNotify("This action is disabled due to Deputy Penalty");
                    return;
                }
                if (currentPlayerConspireActionTaken){
                    ViewUtil.popupNotify("You can only take this action once per turn");
                    return;
                }
                int maxAllowedDice = 3 - currentPlayerActionsTaken;
                int numDice = 1;
                if (maxAllowedDice > 1){
                    Integer[] options = new Integer[maxAllowedDice];
                    for (int i = 0; i < maxAllowedDice; ++i){
                        options[i] = new Integer(i + 1);
                    }
                    numDice = (Integer) ViewUtil.popupDropdown("Conspire Action", "How many dice do you want to roll?", options);
                }
                List<DieResult> dieResults = new ArrayList<>();
                for (int i = 0; i < numDice; ++i){
                    dieResults.add(Util.roll());
                }

                // TODO Show dice results

                dieResults.sort(new Comparator<DieResult>() {
                    @Override
                    public int compare(DieResult dieResult, DieResult t1) {
                        if (dieResult == DieResult.EAGLE)
                            return -1;
                        if (t1 == DieResult.EAGLE)
                            return 1;
                        if (dieResult == DieResult.TARGET)
                            return -1;
                        if (t1 == DieResult.TARGET)
                            return 1;
                        return 0;
                    }
                });
                for (DieResult dieResult: dieResults){
                    switch (dieResult){
                        case EAGLE:{
                            // All conspirators in current player's location gain one suspicion
                            model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer()).getPlayers().stream().forEach(player -> {
                                player.setSuspicion(player.getSuspicion().raise());
                            });
                            // TODO If a player has Defections And Dissent Conspirator Card, it is discarded
                            break;
                        }
                        case TARGET:{
                            // Place on dissent track, gain bonus if full
                            model.getGame().adjDissentTrackDice(1);
                            if (model.getGame().getDissentTrackDice() >= 3){
                                handleFullDissentTrack();
                            }
                            // TODO If a player has Defections and Dissent Conspirator card, ask if they want to put this Target die on it
                            // TODO If two or more dice on this card, decrease Military Support by 1 and return dice to the display
                            break;
                        }
                        default:{
                            currentPlayerActionsAllowed += dieResult.getValue();
                        }
                    }
                }
                currentPlayerConspireActionTaken = true;
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
MOVE
Move from your current space to a connected space.

Legal spaces: Each space on the board has a number on it corresponding to an event stage. You may only move into
spaces with a number equal to or less than the current stage’s number. For example, you can’t enter the PRAGUE space
until Stage 3 or later.
Berlin: Spaces in Berlin are designated by a B instead of a stage number and are always accessible. All Berlin spaces are treated
as being connected to one another. In order to enter or leave Berlin, you must move through the TRAIN STATION.
If an effect simply lists “Berlin” (e.g. “choose one conspirator in Berlin”), this includes all Berlin spaces. However, it is not
considered to be the same space for transfers, plot elements, etc. – conspirators must still be in the same space to fulfill
those conditions.

8
Certain spaces on the board contain modifiers (e.g., Paris: -2 suspicion , +1 Military Support). When you enter a space with
modifiers, you immediately apply the listed modifiers and continue your turn. Applying the effects does not count as an
action. You may gain the effects of a space multiple times in a
turn, but you must move out of the space and then move back
into it to gain the effect again.
Fortified Locations: Certain locations have especially
high security and defense for the Führer. These are
known as fortified locations and serve as required
elements for some plots. Otherwise, they have no
game effect and may be moved into or out of freely.
         */
        view.getGamePanel().getActionPanel().getBtnMove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                while (!move(model.getGame().getCurrentPlayer())){}
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
        ACT
        Resolve one effect in your dossier
         */
        view.getGamePanel().getActionPanel().getBtnPlayCard().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO Choose card to play (maybe a dropdown for now, then let player click on card image)
                // TODO Maybe open separate dialog with card choices
                ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Act (Play Card)", "Choose a card to play", model.getGame().getCurrentPlayer().getDossier().toArray(new ConspiratorCard[0]));
                if (card.getType() == ConspiratorCardType.PLOT){
                    attemptPlot(card);
                }
                else {
                    conspiratorEffectResolver.resolveEffect(card.getEffect());
                    // Discard played card when finished
                    model.getGame().getConspiratorDeck().discard(card);
                }
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        view.getGamePanel().getActionPanel().getBtnPlayLightningCard().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Choose card to play - get all lightning cards from all players
                List<ConspiratorCard> cards =
                        model.getGame().getPlayers().stream()
                                .map(player -> player.getDossier()).flatMap(Collection::parallelStream)
                                .filter(card -> card.getTiming() == ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION)
                                .collect(Collectors.toList());
                // TODO Maybe open separate dialog with card choices
                ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Act (Play Card)", "Choose a card to play", cards.toArray(new ConspiratorCard[0]));
                conspiratorEffectResolver.resolveEffect(card.getEffect());
                // Discard played card when finished
                model.getGame().getConspiratorDeck().discard(card);
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
DOSSIER
Take the top card of the conspirator deck and add it to your dossier face-up, observing dossier limits. See Motivation and Suspicion on page 4.

%ILLEGAL
Conspirator cards with this symbol are illegal.
Possessing these powerful but risky cards could
draw the unwelcome attention of the Gestapo.
(See Gestapo Raids on page 9).

Some conspirator cards are plots, which are necessary to make an assassination attempt. (See Plots on page 10).

Many conspirator cards are discarded after resolving their effect. Place discarded cards into a face-up discard pile next to
the conspirator deck. If the conspirator deck runs out of cards, shuffle the discarded conspirator cards into a new, face-down
conspirator deck.
         */
        view.getGamePanel().getActionPanel().getBtnDrawCard().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Player player = model.getGame().getCurrentPlayer();
                if (player.hasSpecialAbility(PlayerSpecialAbility.ACTION_DRAW_2_CARDS_KEEP_ONE_DISCARD_OTHER)){
                    handlePlayerSpecialAbility(PlayerSpecialAbility.ACTION_DRAW_2_CARDS_KEEP_ONE_DISCARD_OTHER);
                }
                else {
                    ConspiratorCard card = model.getGame().getConspiratorDeck().draw();
                    player.getDossier().add(card);
                }

                int maxCards = player.getDossierMaxSize(model.getGame().getPlayers().size());
                while (player.getDossier().size() > maxCards) {
                    ViewUtil.popupNotify(player.getName() + " has too many cards (hand size limit reached)");
                    ConspiratorCard cardToDiscard = (ConspiratorCard) ViewUtil.popupDropdown("Hand Limit", "You've exceeded your hand limit.  Choose card to discard.", player.getDossier().toArray(new ConspiratorCard[0]));
                    player.getDossier().remove(cardToDiscard);
                    model.getGame().getConspiratorDeck().discard(cardToDiscard);
                }
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
TRANSFER
Give or take one item or card in your dossier to/from a
conspirator in your space, observing item and dossier limits.
(See Item and Dossier Limits on page 5).
When finished taking actions, finish your turn by drawing
an event card.
         */
        view.getGamePanel().getActionPanel().getBtnTransferCardTile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
                if (location.getPlayers().size() == 1){
                    ViewUtil.popupNotify("There is no one else in your location to transfer with");
                    return;
                }

                Player currentPlayer = model.getGame().getCurrentPlayer();

                Player other = null;
                if (location.getPlayers().size() == 2)
                    other = location.getPlayers().get(0) == currentPlayer? location.getPlayers().get(1): location.getPlayers().get(0);
                else {
                    other = (Player) ViewUtil.popupDropdown("Transfer", "Select player to transfer with", location.getPlayers().toArray(new Player[0]));
                }

                if (ViewUtil.popupConfirm("Transfer", "Do you want to transfer a card?")){
                    if (ViewUtil.popupConfirm("Transfer", "Do you want to give a card?")) {
                        if (currentPlayer.getDossier().isEmpty()){
                            ViewUtil.popupNotify("You don't have any cards to give");
                            return;
                        }
                        ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Transfer", "Select card to give", currentPlayer.getDossier().toArray(new ConspiratorCard[0]));
                        other.getDossier().add(card);
                        currentPlayer.getDossier().remove(card);

                        if (other.getDossier().size() > other.getDossierMaxSize(model.getGame().getPlayers().size())){
                            ViewUtil.popupNotify(other.getName() + " has too many cards now, select 1 card to discard");
                            ConspiratorCard cardToDiscard = (ConspiratorCard) ViewUtil.popupDropdown("Transfer", "Select card to discard", other.getDossier().toArray(new ConspiratorCard[0]));
                            other.getDossier().remove(cardToDiscard);
                            model.getGame().getConspiratorDeck().discard(cardToDiscard);
                        }
                    }
                    else {
                        if (other.getDossier().isEmpty()){
                            ViewUtil.popupNotify(other.getName() + " doesn't have any cards to give");
                            return;
                        }
                        ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Transfer", "Select card to give", other.getDossier().toArray(new ConspiratorCard[0]));
                        currentPlayer.getDossier().add(card);
                        other.getDossier().remove(card);

                        if (currentPlayer.getDossier().size() > currentPlayer.getDossierMaxSize(model.getGame().getPlayers().size())){
                            ViewUtil.popupNotify(currentPlayer.getName() + " has too many cards now, select 1 card to discard");
                            ConspiratorCard cardToDiscard = (ConspiratorCard) ViewUtil.popupDropdown("Transfer", "Select card to discard", currentPlayer.getDossier().toArray(new ConspiratorCard[0]));
                            currentPlayer.getDossier().remove(cardToDiscard);
                            model.getGame().getConspiratorDeck().discard(cardToDiscard);
                        }
                    }
                }
                else {
                    if (ViewUtil.popupConfirm("Transfer", "Do you want to give an item?")) {
                        if (currentPlayer.getItems().isEmpty()){
                            ViewUtil.popupNotify("You don't have any items to give");
                            return;
                        }
                        Item item = (Item) ViewUtil.popupDropdown("Transfer", "Select item to give", currentPlayer.getItems().toArray(new Item[0]));
                        other.getItems().add(item);
                        currentPlayer.getItems().remove(item);

                        if (other.getItems().size() > other.getMaxItems(model.getGame().getPlayers().size())){
                            ViewUtil.popupNotify(other.getName() + " has too many items now, select 1 item to discard");
                            Item itemToDiscard = (Item) ViewUtil.popupDropdown("Transfer", "Select item to discard", other.getItems().toArray(new Item[0]));
                            other.getItems().remove(itemToDiscard);
                        }
                    }
                    else {
                        if (other.getItems().isEmpty()){
                            ViewUtil.popupNotify(other.getName() + " doesn't have any items to give");
                            return;
                        }
                        Item item = (Item) ViewUtil.popupDropdown("Transfer", "Select item to give", other.getItems().toArray(new Item[0]));
                        currentPlayer.getItems().add(item);
                        other.getItems().remove(item);

                        if (currentPlayer.getItems().size() > currentPlayer.getMaxItems(model.getGame().getPlayers().size())){
                            ViewUtil.popupNotify(currentPlayer.getName() + " has too many items now, select 1 item to discard");
                            Item itemToDiscard = (Item) ViewUtil.popupDropdown("Transfer", "Select item to discard", currentPlayer.getItems().toArray(new Item[0]));
                            currentPlayer.getItems().remove(itemToDiscard);
                        }
                    }
                }
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
REVEAL ITEM
Flip a face-down item tile in your space face-up.
         */
        view.getGamePanel().getActionPanel().getBtnRevealItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Item item = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer()).getItem();
                if (item == null){
                    ViewUtil.popupNotify("No item to reveal!");
                    return;
                }
                if (item.isRevealed()){
                    ViewUtil.popupNotify("Item is already revealed!");
                    return;
                }
                item.setRevealed(true);
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
COLLECT ITEM
Take a revealed item tile in your space and add it to your conspirator sheet.
         */
        view.getGamePanel().getActionPanel().getBtnCollectItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
                if (location.getItem() == null){
                    ViewUtil.popupNotify("No item to collect");
                    return;
                }
                if (!location.getItem().isRevealed() && !model.getGame().getCurrentPlayer().hasSpecialAbility(PlayerSpecialAbility.ACTION_PICK_UP_UNREVEALED_ITEM_SAME_LOCATION)){
                    ViewUtil.popupNotify("Item must be revealed before collecting");
                    return;
                }
                int maxItems = model.getGame().getCurrentPlayer().getMaxItems(model.getGame().getPlayers().size());
                if (model.getGame().getCurrentPlayer().getItems().size() == maxItems){
                    ViewUtil.popupNotify("You can only hold " + maxItems + " items");
                    return;
                }
                Item item = location.getItem();
                location.setItem(null);
                model.getGame().getCurrentPlayer().getItems().add(item);
                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
DELIVER ITEM
From time to time, you’ll need to continue to carry out your day-to-day duties in order to lower suspicion. Spaces are
marked with a "Deliver" text that may have items delivered there in exchange for lowering suspicion.
Once that space’s original item has been removed and the delivery text is revealed, you may deliver to that city by
discarding the appropriate item(s) and lowering your suspicion by the amount shown. Delivered items are discarded to the
item discard pile and are not put back on the delivery space.
If the benefit is listed as distributed, the active player distributes the benefit among the conspirators however
they choose, regardless of location. However, if a player is in PRISON, they cannot receive any benefits from a delivery.
         */
        view.getGamePanel().getActionPanel().getBtnDeliverItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleItemDelivery();
                run();
            }
        });

        /*
RELEASE
If you are at extreme suspicion, you cannot take this action.

You can attempt to use your standing and influence to order a
conspirator to be released from prison. (See Arrest and Prison
on page 9). You must be in the GESTAPO HQ space in order
to take this action.

Roll one die. If you rolled eagle, you’ve asked too many suspicious
questions and instead of freeing your ally, you are arrested. If
you rolled any other result, raise your suspicion by 1 and choose
one arrested conspirator to release. The released conspirator
moves to GESTAPO HQ at high suspicion.
         */
        view.getGamePanel().getActionPanel().getBtnRelease().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (model.getGame().getCurrentPlayer().getSuspicion() == Suspicion.EXTREME){
                    ViewUtil.popupNotify("You are at EXTREME Suspicion, you cannot take the Release action");
                    return;
                }
                Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
                if (location.getName() != LocationName.GESTAPO_HQ){
                    ViewUtil.popupNotify("You must be at the Gestapo HQ to take the Release action");
                    return;
                }

                List<Player> playersInPrison = model.getGame().getPlayers().stream().filter(player -> player.isArrested()).collect(Collectors.toList());

                DieResult dieResult = Util.roll();
                if (dieResult == DieResult.EAGLE){
                    model.getGame().arrest(model.getGame().getCurrentPlayer());
                }
                else {
                    model.getGame().getCurrentPlayer().setSuspicion(model.getGame().getCurrentPlayer().getSuspicion().raise());
                    // Choose player to release
                    Player other = playersInPrison.get(0);
                    if (playersInPrison.size() > 1)
                        other = (Player) ViewUtil.popupDropdown("Release", "Choose player to release from prison", playersInPrison.toArray(new Player[0]));
                    other.setArrested(false);
                    other.setSuspicion(Suspicion.HIGH);
                    model.getGame().getBoard().move(other, LocationName.GESTAPO_HQ);
                }

                currentPlayerActionsTaken += 1;
                run();
            }
        });

        /*
        ACT
        Resolve one effect on your conspirator sheet preceded by the arrow symbol.
         */
        view.getGamePanel().getActionPanel().getBtnPlayerSpecialAbility().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (specialAbilityActionDisabled){
                    ViewUtil.popupNotify("This action is disabled due to Deputy penalty");
                    return;
                }

                Player player = model.getGame().getCurrentPlayer();
                if (player.getType() == PlayerType.ABWEHR && !model.getGame().isAbwehrUseSpecialAbilities()){
                    ViewUtil.popupNotify("Abwehr can no longer use their special abilities");
                    return;
                }

                if (player.getMotivation().ordinal() < Motivation.MOTIVATED.ordinal()){
                    ViewUtil.popupNotify("You must have motivation at MOTIVATED or above");
                    return;
                }

                PlayerSpecialAbility specialAbility = player.getSpecialAbilities().iterator().next();

                if (player.getSpecialAbilities().size() > 1)
                    specialAbility = (PlayerSpecialAbility) ViewUtil.popupDropdown("Act (Special Ability)", "Choose Special Ability", player.getSpecialAbilities().toArray(new PlayerSpecialAbility[]{}));

                handlePlayerSpecialAbility(specialAbility);

                currentPlayerActionsTaken += 1;
                run();
            }
        });
    }


    /**
     * Progress the game
     */
    public void run(){
        checkGameOver();
        // 1 | Check for Hitler and deputies.
        // 2 | Take up to 3 actions.
        // 3 | Draw an event card.
        while (model.getGame() != null && model.getGame().getPhase() != Phase.GAMEOVER){
            switch (model.getGame().getPhase()){
                case SETUP: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
                case CHECK_FOR_HITLER_DEPUTIES: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            checkHitlerAndDeputies();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            if (model.getGame().getCurrentPlayer().isArrested()){
                                model.getGame().setPhase(Phase.PRISON);
                            }
                            else
                                model.getGame().setPhase(Phase.TAKE_ACTIONS);
                            break;
                        }
                    }
                    break;
                }
                case TAKE_ACTIONS: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            currentPlayerActionsAllowed = 3;
                            if (model.getGame().getCurrentPlayer().hasSpecialAbility(PlayerSpecialAbility.TAKE_4_ACTIONS))
                                currentPlayerActionsAllowed = 4;
                            currentPlayerActionsTaken = 0;
                            currentPlayerConspireActionTaken = false;
                            model.getGame().setPhaseStep(PhaseStep.TAKE_ACTIONS_CHOOSE_ACTION);
                            ViewUtil.popupNotify(model.getGame().getCurrentPlayer().getName() + "'s turn to take actions");
                            break;
                        }
                        case TAKE_ACTIONS_CHOOSE_ACTION: {
                            if (model.getGame().getCurrentPlayer().isArrested()){
                                model.getGame().setPhase(Phase.RESOLVE_EVENT);
                                break;
                            }
                            if (currentPlayerActionsTaken == currentPlayerActionsAllowed){
                                model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                                break;
                            }
                            return;
                        }
                        case END_PHASE: {
                            model.getGame().setPhase(Phase.RESOLVE_EVENT);
                            break;
                        }
                    }
                    break;
                }
                case PRISON:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            DieResult dieResult = Util.roll();
                            boolean resisted = false;
                            boolean released = dieResult == DieResult.TARGET;
                            switch (model.getGame().getCurrentPlayer().getMotivation()){
                                case TIMID:
                                    break;
                                case SKEPTICAL:
                                    resisted = dieResult == DieResult.TARGET;
                                    break;
                                case MOTIVATED:
                                    resisted = dieResult == DieResult.TARGET || dieResult == DieResult.THREE;
                                    break;
                                case COMMITTED:
                                    resisted = dieResult == DieResult.TARGET || dieResult == DieResult.TWO || dieResult == DieResult.THREE;
                                    break;
                                case RECKLESS:
                                    resisted = dieResult != DieResult.EAGLE;
                                    break;
                            }
                            if (released){
                                ViewUtil.popupNotify("The authorities agree to release " + model.getGame().getCurrentPlayer().getName() + " after his interrogation");
                                model.getGame().getCurrentPlayer().setArrested(false);
                            }
                            if (!resisted){
                                ViewUtil.popupNotify(model.getGame().getCurrentPlayer().getName() + " failed to resist interrogation");
                                model.getGame().setPhaseStep(PhaseStep.PRISON_DRAW_INTERROGATION_CARD);
                                break;
                            }
                            else {
                                ViewUtil.popupNotify(model.getGame().getCurrentPlayer().getName() + " successfully resisted interrogation");
                            }
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case PRISON_DRAW_INTERROGATION_CARD: {
                            // Draw an interrogation card (if no option can be applied in-full, choose another card) and choose one option
                            InterrogationCard interrogationCard = model.getGame().getInterrogationDeck().drawFullyApplicable(interrogationEffectResolver);

                            InterrogationEffect effect = (InterrogationEffect)
                                    ViewUtil.popupDropdown("Interrogation", "Choose Effect", interrogationCard.getEffects());
                            interrogationEffectResolver.resolveEffect(effect);
                            model.getGame().getInterrogationDeck().discard(interrogationCard);

                            if (!model.getGame().getCurrentPlayer().isArrested())
                                model.getGame().setPhaseStep(PhaseStep.PRISON_RELEASED);
                            else
                                model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case PRISON_RELEASED:{
                            ViewUtil.popupNotify(model.getGame().getCurrentPlayer().getName() + " has been released from prison");
                            model.getGame().getBoard().move(model.getGame().getCurrentPlayer(), LocationName.GESTAPO_HQ);
                            model.getGame().getCurrentPlayer().setSuspicion(Suspicion.HIGH);
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            model.getGame().setPhase(Phase.RESOLVE_EVENT);
                            break;
                        }
                    }
                }
                case RESOLVE_EVENT: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            for (int i = 0; i < model.getGame().getNumEventCardsToResolve(); ++i)
                                resolveEvent();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            model.getGame().setNumEventCardsToResolve(1);
                            model.getGame().setPhase(Phase.NEXT_PLAYER);
                            break;
                        }
                    }
                    break;
                }
                case NEXT_PLAYER: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            conspireActionDisabled = false;
                            specialAbilityActionDisabled = false;
                            model.getGame().setNextPlayer();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }


    /*
    If you start your turn in the same space as Hitler and/or
one of his five deputies, their watchful eyes and intimidating
presence have disrupted your plans and you suffer the
corresponding penalty(ies):
HITLER Lower your motivation by 1.
HESS Discard one card from your dossier.
GOEBBELS You can't use your special ability this turn.
BORMANN You can't use the Conspire action this turn.
HIMMLER Raise your suspicion by 1.
GOERING Discard one item you are carrying.
It is possible to suffer multiple penalties on your turn.
You choose the order that penalties are applied in.
Penalties are only suffered if Hitler/the deputy is in your
space at the start of your turn. You do not suffer a penalty
if you move onto or off of a space with a leader on it.
     */
    private void checkHitlerAndDeputies(){
        Player currentPlayer = model.getGame().getCurrentPlayer();
        if (currentPlayer.hasSpecialAbility(PlayerSpecialAbility.HITLER_AND_DEPUTY_TOKENS_NO_EFFECT))
            return;
        Location playerLocation = model.getGame().getBoard().getLocationWith(currentPlayer);
        for (NaziMember naziMember: playerLocation.getNaziMembers()) {
            switch (naziMember) {
                case HITLER:
                    applyHitlerPenalty(currentPlayer);
                    break;
                case BORMANN:
                    applyBormannPenalty(currentPlayer);
                    break;
                case GOEBBELS:
                    applyGoebbelsPenalty(currentPlayer);
                    break;
                case GOERING:
                    applyGoeringPenalty(currentPlayer);
                    break;
                case HESS:
                    applyHessPenalty(currentPlayer);
                    break;
                case HIMMLER:
                    applyHimmlerPenalty(currentPlayer);
                    break;
            }
        }
    }

    /**
     * Lower motivation by 1
     * @param player
     */
    private void applyHitlerPenalty(Player player){
        player.setMotivation(player.getMotivation().lower());
    }

    /**
     * You can't use the Conspire action this turn.
     * @param player
     */
    private void applyBormannPenalty(Player player){
        conspireActionDisabled = true;
    }

    /**
     * You can't use your special ability this turn.
     * @param player
     */
    private void applyGoebbelsPenalty(Player player){
        specialAbilityActionDisabled = true;
    }

    /**
     * Discard one item you are carrying.
     * @param player
     */
    private void applyGoeringPenalty(Player player){
        if (model.getGame().getCurrentPlayer().getItems().isEmpty())
            return;
        Item item = null;
        if (model.getGame().getCurrentPlayer().getItems().size() == 1) {
            item = model.getGame().getCurrentPlayer().getItems().get(0);
            ViewUtil.popupNotify("Goering forced you to discard your " + item.getType());
        }
        else
            item = (Item) ViewUtil.popupDropdown("Goering Penalty", "Discard one item", model.getGame().getCurrentPlayer().getItems().toArray(new Item[0]));
        model.getGame().getCurrentPlayer().getItems().remove(item);
    }

    /**
     * Discard one card from your dossier.
     * @param player
     */
    private void applyHessPenalty(Player player){
        if (model.getGame().getCurrentPlayer().getDossier().isEmpty())
            return;
        ConspiratorCard card = null;
        if (model.getGame().getCurrentPlayer().getDossier().size() == 1) {
            card = model.getGame().getCurrentPlayer().getDossier().get(0);
            ViewUtil.popupNotify("Hess forced you to discard your " + card.getName() + " card");
        }
        else
            card = (ConspiratorCard) ViewUtil.popupDropdown("Hess Penalty", "Discard one card", model.getGame().getCurrentPlayer().getDossier().toArray(new ConspiratorCard[0]));
        model.getGame().getCurrentPlayer().getDossier().remove(card);
    }

    /**
     * Raise your suspicion by 1.
     * @param player
     */
    private void applyHimmlerPenalty(Player player){
        player.setSuspicion(player.getSuspicion().raise());
    }

    /**
     Once you've taken all of your actions for the turn, draw the top
card from the lowest numbered (left-most) event deck available
and place it face-up on top of the current event space. Resolve
its effects immediately. This card is the current event, and its
stage number determines the current stage.

A new stage begins when the first card of that stage is the
current event, not when the previous stage’s deck runs out.
Once stage 7 begins, many previously available spaces are
off-limits for the remainder of the game, as the Reich has lost
control over these distant lands. When stage 7 begins, move
Hitler, all deputies, and all conspirators to the nearest legal
space.

     * - If you can’t draw an event card when required to, you lose the game.
     * - If the Stage 7 event Documents Located is drawn*, you lose the game.
     */
    private void resolveEvent(){
        if (model.getGame().getEventCardDeck().isEmpty()){
            ViewUtil.popupNotify("Game Over: Event decks empty!");
            model.getGame().setPhase(Phase.GAMEOVER);
            return;
        }

        // Draw top Event card for current stage
        Deck<EventCard> deck = model.getGame().getEventCardDeck().getStageDeck(model.getGame().getStage());
        if (deck.isEmpty()){
            // Move to next stage
            model.getGame().setStage(model.getGame().getStage() + 1);
            deck = model.getGame().getEventCardDeck().getStageDeck(model.getGame().getStage());

            if (model.getGame().getStage() == 7){
                // TODO When stage 7 begins, move Hitler/deputies/conspirators to nearest legal space (if in location that is not valid)
            }
        }
        EventCard eventCard = deck.draw();

        // Move any KeyEvent over
        if (model.getGame().getCurrentEventCard() != null && model.getGame().getCurrentEventCard().getType() == EventCardType.KEY_EVENT){
            model.getGame().setCurrentKeyEventCard(model.getGame().getCurrentEventCard());
        }

        // Place it face-up in Event space on board
        model.getGame().setCurrentEventEffect(null);
        model.getGame().setCurrentEventCard(eventCard);
        view.refresh();

        // Resolve effects immediately
        eventResolver.resolveEvent(eventCard);
    }

    /**
     * There is only one way to win the game: assassinate Hitler by
     * succeeding at a plot attempt. To succeed at the attempt,
     * you must both roll a number of "targets" on the dice equal to or
     * exceeding the level of Hitler’s Military Support and roll
     * fewer "eagles" than your suspicion allows.
     *
     * However, there are three ways the conspirators lose:
     * - If all players are in prison, you lose the game.
     * - If you can’t draw an event card when required to, you lose the game.
     * - If the Stage 7 event Documents Located is drawn*, you lose the game.
     */
    private void checkGameOver(){
        Location jail = model.getGame().getBoard().getLocation(LocationName.PRISON);
        boolean allPlayersInJail = true;
        for (Player player: model.getGame().getPlayers()){
            if (!jail.getPlayers().contains(player)){
                allPlayersInJail = false;
                break;
            }
        }

        if (allPlayersInJail){
            ViewUtil.popupNotify("Game Over: All players in jail!");
            model.getGame().setPhase(Phase.GAMEOVER);
            return;
        }

        // No Event card and Documents Located Event conditions are checked in RESOLVE_EVENT phase
    }

    /**
     * Once there are 3+ dice on dissent track, current player chooses one:
     * - One conspirator raises motivation by 1
     * - Lower Military Support by 1
     * - Raise all player's motivation by 1 (if current event in play)
     * Remove 3 dice from dissent track
     */
    private void handleFullDissentTrack(){
        if (model.getGame().getDissentTrackDice() < 3){
            return;
        }

        List<DissentReward> rewards = new ArrayList<>();
        rewards.add(DissentReward.RAISE_MOTIVATION);
        rewards.add(DissentReward.LOWER_MILITARY_SUPPORT);
        if (model.getGame().getCurrentEventEffect() == CurrentEventEffect.DISSENT_TRACK_REWARD_ALL_PLAYERS_MAY_INCREASE_MOTIVATION_BY_1){
            rewards.add(DissentReward.ALL_PLAYERS_INCREASE_MOTIVATION);
        }

        DissentReward reward =
                (DissentReward) ViewUtil.popupDropdown("Dissent Track", "Choose reward",
                        rewards.toArray(new DissentReward[0]));

        switch (reward){
            case RAISE_MOTIVATION:{
                Player selected = (Player) ViewUtil.popupDropdown("Dissent Track", "Choose Conspirator to raise Motivation", model.getGame().getPlayers().toArray(new Player[0]));
                selected.setMotivation(selected.getMotivation().raise());
                break;
            }
            case LOWER_MILITARY_SUPPORT:{
                model.getGame().adjMilitarySupport(-1);
                break;
            }
            case ALL_PLAYERS_INCREASE_MOTIVATION:{
                model.getGame().getPlayers().stream().forEach(player -> {
                    player.setMotivation(player.getMotivation().raise());
                });
                break;
            }
        }
        model.getGame().adjDissentTrackDice(-3);
    }

    private void handleItemDelivery(){
        Player player = model.getGame().getCurrentPlayer();
        Location location = model.getGame().getBoard().getLocationWith(player);
        if (location.getItem() != null){
            ViewUtil.popupNotify("You must remove the Item before you can deliver items here");
            return;
        }
        if (player.getItems().isEmpty()){
            ViewUtil.popupNotify("You have no items to deliver");
            return;
        }
        ItemType deliveryItem = location.getDeliveryItem();
        Item selectedItem = null;
        if (deliveryItem == ItemType.ANY){
            selectedItem = (Item) ViewUtil.popupDropdown("Item Delivery", "Choose Item to deliver", player.getItems().toArray(new Item[0]));
        }
        else {
            Optional<Item> optional = player.getItems().stream().filter(item -> item.getType() == deliveryItem).findFirst();
            if (!optional.isPresent()){
                ViewUtil.popupNotify("You don't have a " + deliveryItem + " to deliver");
                return;
            }
            selectedItem = optional.get();
        }
        player.getItems().remove(selectedItem);
        applyLocationModifier(location, location.getDeliveryModifier());
        currentPlayerActionsTaken += 1;
    }

    private void applyLocationModifier(Location location, LocationModifier locationModifier){
        Player player = model.getGame().getCurrentPlayer();
        switch (locationModifier){
            case SUSPICION_MINUS_2:
                player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_STAGE_2:
                if (model.getGame().getStage() == 2)
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_STAGE_1:
                if (model.getGame().getStage() == 1)
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_IF_ABWEHR:
                if (player.getType() == PlayerType.ABWEHR)
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_IF_CIVILIAN:
                if (player.getType() == PlayerType.CIVILIAN)
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_IF_WEHRMACHT:
                if (player.getType() == PlayerType.WEHRMACHT)
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_IF_DEPUTY_PRESENT:
                if (location.getNaziMembers().stream().anyMatch(naziMember -> naziMember != NaziMember.HITLER))
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_2_IF_HITLER_PRESENT:
                if (location.getNaziMembers().stream().anyMatch(naziMember -> naziMember == NaziMember.HITLER))
                    player.setSuspicion(player.getSuspicion().lower().lower());
                break;
            case SUSPICION_MINUS_3_MILITARY_SUPPORT_PLUS_1:
                player.setSuspicion(player.getSuspicion().lower().lower());
                model.getGame().adjMilitarySupport(1);
                break;
            case SUSPICION_MINUS_3_DISTRIBUTED:
                // TODO Distribute among players
                player.setSuspicion(player.getSuspicion().lower().lower().lower());
                break;
            case SUSPICION_PLUS_1:
                player.setSuspicion(player.getSuspicion().raise());
                break;
            case MOTIVATION_PLUS_2_SUSPICION_PLUS_1:
                player.setMotivation(player.getMotivation().raise().raise());
                player.setSuspicion(player.getSuspicion().raise());
                break;
        }
    }

    private void attemptPlot(ConspiratorCard card){
        Plot plot = model.getGame().getPlots().get(card.getId());
        if (!plot.requirementsMet(model.getGame())) {
            ViewUtil.popupNotify("You don't meet the requirements to play this Plot");
            return;
        }

        // Collect Dice
        int numDice = 1 + plot.getNumFreeExtraDice(model.getGame());
        List<Item> items = plot.getItemsAvailableForExtraDice(model.getGame());
        while (!items.isEmpty() && ViewUtil.popupConfirm("Plot Attempt", "Do you want to discard an item to add a die?")){
            Item item = (Item) ViewUtil.popupDropdown("Plot Attempt", "Choose Item to discard", items.toArray(new Item[0]));
            numDice += 1;
            model.getGame().getCurrentPlayer().getItems().remove(item);
            items.remove(item);
        }

        // TODO Add dice granted by conspirator cards

        // Add die if player has special ability
        Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
        for (Player player: location.getPlayers()){
            if (player.hasSpecialAbility(PlayerSpecialAbility.ADD_DIE_TO_PLOT_IN_SAME_LOCATION)){
                numDice += 1;
                break;
            }
        }

        // TODO Allow user to choose how many dice to roll (max == numDice)

        List<DieResult> dieResults = new ArrayList<>();
        for (int i = 0; i < numDice; ++i){
            dieResults.add(Util.roll());
        }

        // Failure (Detection): Num Eagles >= Suspicion
        int numEagles = (int) dieResults.stream().filter(dieResult -> dieResult == DieResult.EAGLE).count();
        int numTargets = (int) dieResults.stream().filter(dieResult -> dieResult == DieResult.TARGET).count();

        // Apply current event modifiers
        if (model.getGame().getCurrentEventEffect() != null){
            switch (model.getGame().getCurrentEventEffect()){
                case IGNORE_1_EAGLE_ON_ALL_PLOT_ATTEMPTS:
                    numEagles -= 1;
                    break;
                case IGNORE_2_EAGLES_ON_ALL_PLOT_EVENTS:
                    numEagles -= 2;
                    break;
                case ADD_1_EAGLE_TO_ALL_PLOT_ATTEMPTS:
                    numEagles += 1;
                    break;
            }
        }

        if (numEagles >= model.getGame().getCurrentPlayer().getSuspicion().getNumEaglesForPlotFailure()) {
            ViewUtil.popupNotify("Plot has been detected!  Player arrested!");
            // Discard plot
            model.getGame().getConspiratorDeck().discard(card);
            model.getGame().getCurrentPlayer().getDossier().remove(card);
            // Move hitler to Chancellery
            model.getGame().getBoard().move(NaziMember.HITLER, LocationName.CHANCELLERY);
            // all conspirators lose 1 motivation
            model.getGame().getPlayers().stream().forEach(player -> {
                player.setMotivation(player.getMotivation().lower());
            });
            // Arrest current player
            model.getGame().arrest(model.getGame().getCurrentPlayer());
        }
        // Success == Num Targets >= Military support
        else if (numTargets >= model.getGame().getMilitarySupport()){
            ViewUtil.popupNotify("Hitler has been assassinated!  Players Win!");
            model.getGame().setPhase(Phase.GAMEOVER);
        }
    }


    private void handlePlayerSpecialAbility(PlayerSpecialAbility specialAbility){
        switch (specialAbility){
            case ACTION_INCREASE_MOTIVATION_OF_OTHER_CONSPIRATORS_SAME_LOCATION: {
                Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
                location.getPlayers().stream().filter(player -> player != model.getGame().getCurrentPlayer()).forEach(player -> {
                    player.setMotivation(player.getMotivation().raise());
                });
                break;
            }
            case ACTION_DISCARD_ITEM_TO_LOWER_SUSPICION_OF_ANY_PLAYER: {
                // Choose Item to discard
                Item item = (Item) ViewUtil.popupDropdown("Act (Special Ability)", "Choose Item to discard", model.getGame().getCurrentPlayer().getItems().toArray(new Item[0]));
                model.getGame().getCurrentPlayer().getItems().remove(item);
                // Choose player to lower suspicion
                Player player = (Player) ViewUtil.popupDropdown("Act (Special Ability)", "Choose player to reduce suspicion", model.getGame().getPlayers().toArray(new Player[0]));
                player.setSuspicion(player.getSuspicion().lower());
                break;
            }
            case ACTION_REARRANGE_TOP_THREE_EVENT_CARDS: {
                // TODO Implement this
                break;
            }
            case ACTION_MOVE_2_SPACES_OR_MOVE_ANOTHER_CONSPIRATOR_1_SPACE: {
                String move2spaces = "Move 2 locations";
                String moveAnother = "Move another player 1 location";
                String choice = (String) ViewUtil.popupDropdown("Special Ability", "Choose Action", new String[]{move2spaces, moveAnother});
                if (choice.equals(moveAnother)){
                    Player player = (Player) ViewUtil.popupDropdown("Special Ability", "Move which player?", model.getGame().getPlayers().toArray(new Player[0]));
                    while (!move(player)){}
                }
                else{
                    while (!move(model.getGame().getCurrentPlayer())){}
                    view.refresh();
                    while (!move(model.getGame().getCurrentPlayer())){}
                }
                view.refresh();
                break;
            }
            case ACTION_DRAW_2_CARDS_KEEP_ONE_DISCARD_OTHER: {
                int maxCards = model.getGame().getCurrentPlayer().getDossierMaxSize(model.getGame().getPlayers().size());
                if (model.getGame().getCurrentPlayer().getDossier().size() == maxCards){
                    ViewUtil.popupNotify("You cannot take any more cards (hand size limit reached)");
                    return;
                }
                ConspiratorCard card1 = model.getGame().getConspiratorDeck().draw();
                ConspiratorCard card2 = model.getGame().getConspiratorDeck().draw();
                ConspiratorCard keep = (ConspiratorCard) ViewUtil.popupDropdown("Draw Card", "Choose a card to keep", new ConspiratorCard[]{card1, card2});
                model.getGame().getCurrentPlayer().getDossier().add(keep);
                model.getGame().getConspiratorDeck().discard(keep == card1? card2: card1);
                break;
            }
            case ACTION_PICK_UP_UNREVEALED_ITEM_SAME_LOCATION: {
                Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
                if (location.getItem() == null){
                    ViewUtil.popupNotify("No item to collect");
                    return;
                }
                int maxItems = model.getGame().getCurrentPlayer().getMaxItems(model.getGame().getPlayers().size());
                if (model.getGame().getCurrentPlayer().getItems().size() == maxItems){
                    ViewUtil.popupNotify("You can only hold " + maxItems + " items");
                    return;
                }
                Item item = location.getItem();
                item.setRevealed(true);
                location.setItem(null);
                model.getGame().getCurrentPlayer().getItems().add(item);
                break;
            }
            case ACTION_REVEAL_ANY_ITEM_TILE: {
                List<Location> locations =
                        model.getGame().getBoard().getLocations().values().stream()
                                .filter(location -> location.getItem() != null && !location.getItem().isRevealed())
                                .collect(Collectors.toList());
                Location location = (Location) ViewUtil.popupDropdown("Reveal Item", "Select location to reveal the item", locations.toArray(new Location[0]));
                location.getItem().setRevealed(true);
                break;
            }
        }
    }


    private boolean move(Player player){
        Location myLocation = model.getGame().getBoard().getLocationWith(player);
        Set<LocationName> connectionNames = model.getGame().getBoard().getConnections(myLocation.getName());
        LocationName destName = (LocationName) ViewUtil.popupDropdown("Move", "Where do you want to move to?", connectionNames.toArray(new LocationName[0]));
        Location dest = model.getGame().getBoard().getLocation(destName);
        if (model.getGame().getStage() >= dest.getMinStage() && model.getGame().getStage() <= dest.getMaxStage()){
            model.getGame().getBoard().move(player, destName);

            if (dest.getLocationModifier() != null){
                applyLocationModifier(dest, dest.getLocationModifier());
            }

            if (model.getGame().getCurrentEventEffect() == CurrentEventEffect.MOVE_TO_NUREMBERG_SET_SUSPICION_TO_LOW){
                player.setSuspicion(Suspicion.LOW);
            }
        }
        else {
            ViewUtil.popupNotify("That location is locked");
            return false;
        }
        return true;
    }
}
