package bo.game.conspirator;

import bo.CommonController;
import bo.Model;
import bo.game.Deck;
import bo.game.NaziMember;
import bo.game.conspirator.ConspiratorCardEffect;
import bo.game.event.EventCard;
import bo.game.event.EventCardDeck;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.location.LocationName;
import bo.game.player.Player;
import bo.game.player.Suspicion;
import bo.game.util.DieResult;
import bo.game.util.Util;
import bo.view.View;
import bo.view.util.ViewUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConspiratorEffectResolver {
    private Model model;
    private View view;
    private CommonController commonController;

    public ConspiratorEffectResolver(Model model, View view, CommonController commonController){
        this.model = model;
        this.view = view;
        this.commonController = commonController;
    }

    /**
     * Resolve the card
     * @param card
     * @return true if resolved successfully, false if not (no action should be taken and card should not be discarded)
     */
    public ConspiratorEffectResolution resolveEffect(ConspiratorCard card){
        switch (card.getEffect()){
            case DEFECTIONS_AND_DISSENT:
                // Handled in-line in Controller::conspire action
                break;
            case FALSE_ACCUSATIONS:
                // Handled in-line in Controller::interrogation flow
                break;
            case BLACK_ORCHESTRA:
                return handleBlackOrchestra();
            case PLANNING_SESSION:
                return handlePlanningSession();
            case QUICK_REACTION:
                // Handled in-line in Controller::attemptPlot
                break;
            case EFFECTIVE_PLANNING:
                return handleEffectivePlanning();
            case DUMPSTER_DIVE:
                return handleDumpsterDive();
            case DELAY_HITLER:
                // Handled in-line in EffectResolver
                break;
            case SLOW_NEWS_DAY:
                return handleSlowNewsDay();
            case SAFE:
                // Handled in-line in EffectResolver::handleGestapoRaid
                break;
            case AIRPLANE_ACCESS:
                return handleAirplaneAccess();
            case HITLERS_SCHEDULE_LEAKED:
                // Handled in-line in Controller::attemptPlot
                break;
            case INFILTRATE_BUNKER:
                return handleInfiltrateBunker();
            case BLACK_MARKET:
                return handleBlackMarket();
            case COVER_STORY:
                return handleCoverStory();
            case HISTORY_REPEATS:
                return handleHistoryRepeats(card);
            case CALLED_AWAY:
                return handleCalledAway();
            case DISTANT_CONTACT:
                return handleDistantContact();
            case LOSE_GESTAPO:
                return handleLoseGestapo();
            case LONE_REVOLVER:
                return handleLoneRevolver();
            case SUITCASE_BOMB:
                return handleSuitcaseBomb();
            case DERAIL_TRAIN:
                return handleDerailTrain();
            case PLANE_BOMB:
                return handlePlaneBomb();
            case HIDDEN_BOMB:
                return handleHiddenBomb();
            case COUP:
                return handleCoup();
            case POISON_GAS:
                return handlePoisonGas();
            case SNIPER:
                return handleSniper();
            case KIDNAPPING:
                return handleKidnapping();
            case POISON_PARCEL:
                return handlePoisonParcel();
            case POISON_FOOD:
                return handlePoisonFood();
            case AMBUSH:
                return handleAmbush();
            case PARTISAN_INFORMANT:
                // Handled in-line at Controller::attemptPlot
                break;
            case CLASSIFIED_PAPERS:
                // Handled in-line at Controller::conspire
                break;
            case WAR_CRIMES_EVIDENCE:
                return handleWarCrimesEvidence();
            case FORGED_DOCUMENTS:
                return handleForgedDocuments();
            case MILITARY_SECRETS:
                return handleMilitarySecrets();
            case INTELLIGENCE_BRIEFING:
                return handleIntelligenceBriefing();
            case HIDDEN_CAMERA:
                return handleHiddenCamera();
            case FALSE_SAFETY_REPORT:
                return handleFalseSafetyReport();
            case INSPIRING_LETTER:
                return handleInspiringLetter();
            case OFFICER_RECRUITED:
                // Handled in-line at Controller::attemptPlot
                break;
            case CONCEALED_PISTOL:
                return handleConcealedPistol();
            case FORGED_RELEASE_ORDERS:
                return handleForgedReleaseOrders();
            default:
                break;
        }
        return ConspiratorEffectResolution.CANCELLED;
    }

    private ConspiratorEffectResolution handleBlackOrchestra(){
        // All conspirators must be on same space
        List<Location> locationList = model.getGame().getBoard().getLocations().values().stream().filter(location -> !location.getPlayers().isEmpty()).collect(Collectors.toList());
        if (locationList.size() > 1){
            ViewUtil.popupNotify("All players must be on the same location to play this card");
            return ConspiratorEffectResolution.CANCELLED;
        }
        // roll all non-committed dice
        int numDice = model.getGame().getNumDiceAvailable();
        List<DieResult> dieResults = Util.roll(numDice);
        Iterator<DieResult> iterator = dieResults.listIterator();
        while (iterator.hasNext()) {
            DieResult dieResult = iterator.next();
            // raise all conspirator's suspicion by 1 for each eagle
            if (dieResult == DieResult.EAGLE){
                model.getGame().getPlayers().stream().forEach(player -> player.raiseSuspicion());
                iterator.remove();
            }
            // all targets rolled go on the dissent track
            else if (dieResult == DieResult.TARGET){
                model.getGame().adjDissentTrackDice(1);
                iterator.remove();
            }
        }

        if (model.getGame().getDissentTrackDice() >= 3){
            commonController.handleFullDissentTrack();
        }

        // number dice may be distributed to conspirators to be used on their next turn
        for (DieResult dieResult: dieResults){
            Player selected = (Player) ViewUtil.popupDropdown("Black Orchestra", "Assign " + dieResult.getValue() + " bonus actions to player", model.getGame().getPlayers().toArray(new Player[0]));
            model.getGame().getBonusActions().put(selected, model.getGame().getBonusActions().getOrDefault(selected, 0) + dieResult.getValue());
        }

        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handlePlanningSession(){
        // Reveal cards until you find a Plot
        // Add it to your hand and discard other revealed cards
        List<ConspiratorCard> revealedCards = new ArrayList<>();
        ConspiratorCard card = null;
        while ((card = model.getGame().getConspiratorDeck().draw()) != null){
            if (card.getType() == ConspiratorCardType.PLOT){
                model.getGame().getCurrentPlayer().getDossier().add(card);
                break;
            }
            else {
                revealedCards.add(card);
            }
        }
        model.getGame().getConspiratorDeck().discardAll(revealedCards);
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleEffectivePlanning(){
        model.getGame().adjCurrentPlayerActionsAllowed(1);
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleDumpsterDive(){
        // Take any card from the discard pile and add to dossier
        ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Dumpster Dive", "Choose card to add to hand", model.getGame().getConspiratorDeck().getDiscardPile().toArray(new ConspiratorCard[0]));
        model.getGame().getConspiratorDeck().getDiscardPile().remove(card);
        model.getGame().getCurrentPlayer().getDossier().add(card);
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleSlowNewsDay(){
        model.getGame().setNumEventCardsToResolve(0);
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleAirplaneAccess(){
        // Move to any legal space on board
        List<Location> locations = model.getGame().getBoard().getLegalLocations(model.getGame().getStage());
        Location selected = (Location) ViewUtil.popupDropdown("Airplane Access", "Choose location to move to", locations.toArray(new Location[0]));
        model.getGame().getBoard().move(model.getGame().getCurrentPlayer(), selected.getName());
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleInfiltrateBunker(){
        // Draw up to 3 cards and raise suspicion by one
        model.getGame().getCurrentPlayer().raiseSuspicion();

        for (int i = 0; i < 3; ++i){
            if (ViewUtil.popupConfirm("Infiltrate Secret SS Bunker", "Do you want to draw a card? (x3)")){
                commonController.drawConspiratorCard(model.getGame().getCurrentPlayer());
            }
            else
                break;
        }
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleBlackMarket(){
        // Take a discarded item tile
        Item selected = (Item) ViewUtil.popupDropdown("Black Market", "Choose Item to take", model.getGame().getItemDeck().getDiscardPile().toArray(new Item[0]));
        model.getGame().getItemDeck().getDiscardPile().remove(selected);
        model.getGame().getCurrentPlayer().getItems().add(selected);
        commonController.checkNumItems(model.getGame().getCurrentPlayer());
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleCoverStory(){
        // All conspirators in current player's space lower suspicion by one
        Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
        location.getPlayers().stream().forEach(player -> {
            player.lowerSuspicion();
        });
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleHistoryRepeats(ConspiratorCard card){
        // Shuffle all discarded conspirator cards (including this one) back into the deck and draw a card
        model.getGame().getConspiratorDeck().discard(card);
        model.getGame().getConspiratorDeck().shuffleDiscardIntoDeck();
        ConspiratorCard card1 = model.getGame().getConspiratorDeck().draw();
        model.getGame().getCurrentPlayer().getDossier().add(card1);
        return ConspiratorEffectResolution.DONE_NO_DISCARD;
    }

    private ConspiratorEffectResolution handleCalledAway(){
        // Move any two deputies to any one location
        List<NaziMember> naziMembers = new ArrayList<>();
        for (int i = 0; i < 2; ++i){
            NaziMember deputy =
                    (NaziMember) ViewUtil.popupDropdown(
                            "Called Away",
                            "Select Deputy to move",
                            model.getGame().getDeputiesInPlay().stream().filter(naziMember -> !naziMembers.contains(naziMember)).collect(Collectors.toList()).toArray(new NaziMember[0]));
            naziMembers.add(deputy);
        }
        Location location =
                (Location) ViewUtil.popupDropdown(
                        "Called Away",
                        "Select location to place deputies",
                        model.getGame().getBoard().getLegalLocations(model.getGame().getStage()).toArray(new Location[0]));
        for (NaziMember naziMember: naziMembers){
            model.getGame().getBoard().move(naziMember, location.getName());
        }
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleDistantContact(){
        // Take any item (revealed or unrevealed) from any location
        List<Location> locations = model.getGame().getBoard().getLocations().values().stream().filter(location -> location.getItem() != null).collect(Collectors.toList());
        Location selected = (Location) ViewUtil.popupDropdown("Distant Contact", "Collect item from a location", locations.toArray(new Location[0]));
        Item item = selected.getItem();
        item.setRevealed(true);
        selected.setItem(null);
        model.getGame().getCurrentPlayer().getItems().add(item);
        commonController.checkNumItems(model.getGame().getCurrentPlayer());
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleLoseGestapo(){
        model.getGame().getCurrentPlayer().lowerSuspicion();
        return ConspiratorEffectResolution.DONE;
    }

    // Plots
    private ConspiratorEffectResolution handleLoneRevolver(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleSuitcaseBomb(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleDerailTrain(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handlePlaneBomb(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleHiddenBomb(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleCoup(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handlePoisonGas(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleSniper(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleKidnapping(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handlePoisonParcel(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handlePoisonFood(){return ConspiratorEffectResolution.CANCELLED;}
    private ConspiratorEffectResolution handleAmbush(){return ConspiratorEffectResolution.CANCELLED;}

    // Restricted cards
    private ConspiratorEffectResolution handleWarCrimesEvidence(){
        // Choose A) Decrease Military Support by 1 or B) All conspirators in location gain 1 motivation
        final String decreaseMilitarySupport = "Decrease Military Support by 1";
        final String raiseMotivation = "All conspirators in your space gain 1 motivation";
        String choice = (String) ViewUtil.popupDropdown("War Crimes Evidence", "Select option", new String[]{decreaseMilitarySupport, raiseMotivation});
        if (choice.equals(decreaseMilitarySupport)) {
            model.getGame().adjMilitarySupport(-1);
        }
        else {
            Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
            location.getPlayers().stream().forEach(player -> {
                player.raiseMotivation();
            });
        }
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleForgedDocuments(){
        // Choose A) Move Hitler up to 3 spaces or B) Move any conspirator up to 3 spaces
        final String moveHitler = "Move Hitler up to 3 spaces";
        final String moveConspirator = "Move a conspirator up to 3 spaces";
        String choice = (String) ViewUtil.popupDropdown("Forged Documents", "Select option", new String[]{moveHitler, moveConspirator});
        if (choice.equals(moveHitler)) {
            LocationName hitlerLocation = model.getGame().getBoard().getLocationWith(NaziMember.HITLER).getName();
            for (int i = 0; i < 3; ++i){
                Set<LocationName> connections = model.getGame().getBoard().getConnections(hitlerLocation);
                hitlerLocation = (LocationName) ViewUtil.popupDropdown("Forged Documents", "Move Hitler to (3x)", connections.toArray(new LocationName[0]));
                model.getGame().getBoard().move(NaziMember.HITLER, hitlerLocation);
            }
        }
        else {
            Player selected = (Player) ViewUtil.popupDropdown("Forged Documents", "Select conspirator to move", model.getGame().getPlayers().toArray(new Player[0]));
            LocationName playerLocation = model.getGame().getBoard().getLocationWith(selected).getName();
            for (int i = 0; i < 3; ++i){
                Set<LocationName> connections = model.getGame().getBoard().getConnections(playerLocation);
                playerLocation = (LocationName) ViewUtil.popupDropdown("Forged Documents", "Move " + selected.getName() + " to (3x)", connections.toArray(new LocationName[0]));
                model.getGame().getBoard().move(selected, playerLocation);
            }
        }
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleMilitarySecrets(){
        // Play while in Zurich or Stockholm
        // Decrease Military Support by 1
        Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
        if (location.getName() == LocationName.ZURICH || location.getName() == LocationName.STOCKHOLM){
            model.getGame().adjMilitarySupport(-1);
            return ConspiratorEffectResolution.DONE;
        }
        ViewUtil.popupNotify("You must be in Zurich or Stockholm to play this card");
        return ConspiratorEffectResolution.CANCELLED;
    }
    private ConspiratorEffectResolution handleIntelligenceBriefing(){
        // Look at top two Event cards of current stage and choose
        // A) Remove one card from game and put other back on top of deck
        // B) Arrange cards and place both on top of deck
        final String removeA = "Remove 1st card and put 2nd card back on top of deck";
        final String removeB = "Remove 2nd card and put 1st card back on top of deck";
        final String aThenB = "Place 1st card on top and 2nd card under it";
        final String bThenA = "Place 2nd card on top and 1st card under it";
        Deck<EventCard> deck = model.getGame().getEventCardDeck().getStageDeck(model.getGame().getStage());
        EventCard card1 = deck.draw();
        EventCard card2 = deck.draw();
        String choice = null;
        if (card1 == null){
            // deck is empty
            ViewUtil.popupNotify("Event deck is empty, cannot play this card");
            return ConspiratorEffectResolution.CANCELLED;
        }
        if (card2 == null){
            choice = removeB;
            ViewUtil.popupNotify("Only one card left, leaving it on top");
        }
        if (choice == null)
            choice = (String) ViewUtil.popupDropdown("Intelligence Briefing", "1st: " + card1 + "\n2nd: " + card2, new String[]{removeA, removeB, aThenB, bThenA});
        if (choice.equals(removeA)){
            deck.addToTop(card2);
        }
        else if (choice.equals(removeB)){
            deck.addToTop(card1);
        }
        else if (choice.equals(aThenB)){
            deck.addToTop(card2);
            deck.addToTop(card1);
        }
        else if (choice.equals(bThenA)){
            deck.addToTop(card1);
            deck.addToTop(card2);
        }
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleHiddenCamera(){
        // Play while in Auschwitz or Treblinka
        // All conspirators gain one motivation
        Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
        if (location.getName() == LocationName.AUSCHWITZ || location.getName() == LocationName.TREBLINKA){
            model.getGame().getPlayers().stream().forEach(player -> {
                player.raiseMotivation();
            });
            return ConspiratorEffectResolution.DONE;
        }
        ViewUtil.popupNotify("You must be in Auschwitz or Treblinka to play this card");
        return ConspiratorEffectResolution.CANCELLED;
    }

    private ConspiratorEffectResolution handleFalseSafetyReport(){
        model.getGame().getPlayers().stream().forEach(player -> {
            player.lowerSuspicion();
        });
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleInspiringLetter(){
        Location location = model.getGame().getBoard().getLocationWith(model.getGame().getCurrentPlayer());
        location.getPlayers().stream().forEach(player -> {
            player.raiseMotivation();
        });
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleConcealedPistol(){
        Item concealedPistol = new Item(ItemType.WEAPONS);
        concealedPistol.setConcealed(true);
        model.getGame().getCurrentPlayer().getItems().add(concealedPistol);
        return ConspiratorEffectResolution.DONE;
    }

    private ConspiratorEffectResolution handleForgedReleaseOrders(){
        // Release an arrested conspirator regardless of your location
        List<Player> playersInPrison = model.getGame().getBoard().getLocation(LocationName.PRISON).getPlayers();
        Player released = (Player) ViewUtil.popupDropdown("Forged Release Orders", "Select prisoner to release", playersInPrison.toArray(new Player[0]));
        released.setArrested(false);
        model.getGame().getBoard().move(released, LocationName.GESTAPO_HQ);
        // set their suspicion to medium
        released.setSuspicion(Suspicion.MEDIUM);
        return ConspiratorEffectResolution.DONE;
    }
}
