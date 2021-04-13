package bo;

import bo.game.Difficulty;
import bo.game.Game;
import bo.game.Phase;
import bo.game.PhaseStep;
import bo.game.location.LocationName;
import bo.game.player.Player;
import bo.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        view.getMainMenuPanel().getBtnNewGame().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Game game = new Game();
                model.setGame(game);

                // TODO Allow user to set difficulty and player roles
                game.setDifficulty(Difficulty.STANDARD);
                game.getPlayers().add(new Player(Player.BECK));
                game.getPlayers().add(new Player(Player.OSTER));

                // Add the player board panels
                for (Player player: game.getPlayers()){
                    view.getGamePanel().getPlayerBoardsPanel().addPlayerBoard(player);
                    game.getBoard().getLocation(LocationName.TRAIN_STATION).getPlayers().add(player);
                }

                view.showGame();
                view.refresh();
            }
        });
    }

    public void run(){
        // 1 | Check for Hitler and deputies.
        // 2 | Take up to 3 actions.
        // 3 | Draw an event card.
        while (model.getGame() != null && model.getGame().getPhase() != Phase.GAMEOVER){
            switch (model.getGame().getPhase()){
                case SETUP -> {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE -> {
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE -> {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
                case CHECK_FOR_HITLER_DEPUTIES -> {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE -> {
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
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE -> {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
                case TAKE_ACTIONS -> {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE -> {
                            model.getGame().setPhaseStep(PhaseStep.TAKE_ACTIONS_CHOOSE_ACTION);
                            break;
                        }
                        case TAKE_ACTIONS_CHOOSE_ACTION -> {
                            /*
                            ACT
Resolve one effect in your dossier or on your conspirator sheet
preceded by the ~ symbol.
] EFFECTS | An effect preceded by the ] symbol does not
require an action to resolve. ] effects can be resolved at any
time, even on another player’s turn.
COLLECT ITEM
Take a revealed item tile in your space and add it to your
conspirator sheet.
CONSPIRE
You can only take the Conspire action once during your turn.
Take up to 3 dice, spending one action for each die taken.
Roll all of those dice and resolve the results in this order:
■■ For each # rolled , you and all other conspirators in
your space raise suspicion by 1.
■■ For each @ rolled, place that die on the Dissent Track.
■■ Total all the number results and gain that many
actions this turn, plus any unspent actions (if any)
that you didn't use for the Conspire action.

DELIVER ITEM
From time to time, you’ll need to continue to carry out your
day-to-day duties in order to lower suspicion. Spaces are
marked with a "Deliver" text that may have items delivered
there in exchange for lowering suspicion.
Once that space’s original item has been removed and the
delivery text is revealed, you may deliver to that city by
discarding the appropriate item(s) and lowering your suspicion
by the amount shown. Delivered items are discarded to the
item discard pile and are not put back on the delivery space.
If the benefit is listed as distributed, the active player
distributes the benefit among the conspirators however
they choose, regardless of location. However, if a player is in
PRISON, they cannot receive any benefits from a delivery.
DOSSIER
Take the top card of the conspirator deck and add it to your
dossier face-up, observing dossier limits. See Motivation and
Suspicion on page 4.
%ILLEGAL
Conspirator cards with this symbol are illegal.
Possessing these powerful but risky cards could
draw the unwelcome attention of the Gestapo.
(See Gestapo Raids on page 9).
Some conspirator cards are plots, which are necessary to make
an assassination attempt. (See Plots on page 10).
Many conspirator cards are discarded after resolving their
effect. Place discarded cards into a face-up discard pile next to
the conspirator deck. If the conspirator deck runs out of cards,
shuffle the discarded conspirator cards into a new, face-down
conspirator deck.
MOVE
Move from your current space to a connected space.
Legal spaces: Each space on the board has a number on it
corresponding to an event stage. You may only move into
spaces with a number equal to or less than the current stage’s
number. For example, you can’t enter the PRAGUE space
until Stage 3 or later.
Berlin: Spaces in Berlin are designated by a B instead of a stage
number and are always accessible. All Berlin spaces are treated
as being connected to one another. In order to enter or leave
Berlin, you must move through the TRAIN STATION.
If an effect simply lists “Berlin” (e.g. “choose one conspirator
in Berlin”), this includes all Berlin spaces. However, it is not
considered to be the same space for transfers, plot elements,
etc. – conspirators must still be in the same space to fulfill
those conditions.
8
Certain spaces on the board contain modifiers (e.g., Paris:
-2 suspicion , +1 Military Support). When you enter a space with
modifiers, you immediately apply the listed modifiers and
continue your turn. Applying the effects does not count as an
action. You may gain the effects of a space multiple times in a
turn, but you must move out of the space and then move back
into it to gain the effect again.
Fortified Locations: Certain locations have especially
high security and defense for the Führer. These are
known as fortified locations and serve as required
elements for some plots. Otherwise, they have no
game effect and may be moved into or out of freely.
RELEASE
$ | If you are at extreme suspicion, you cannot take this action.
You can attempt to use your standing and influence to order a
conspirator to be released from prison. (See Arrest and Prison
on page 9). You must be in the GESTAPO HQ space in order
to take this action.
Roll one die. If you rolled # , you’ve asked too many suspicious
questions and instead of freeing your ally, you are arrested. If
you rolled any other result, raise your suspicion by 1 and choose
one arrested conspirator to release. The released conspirator
moves to GESTAPO HQ at high suspicion.
REVEAL ITEM
Flip a face-down item tile in your space face-up.
TRANSFER
Give or take one item or card in your dossier to/from a
conspirator in your space, observing item and dossier limits.
(See Item and Dossier Limits on page 5).
When finished taking actions, finish your turn by drawing
an event card.
                             */
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE -> {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
                case RESOLVE_EVENT -> {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE -> {
                            /*
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
                             */
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE -> {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
                case NEXT_PLAYER -> {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE -> {
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE -> {
                            model.getGame().setPhase(Phase.CHECK_FOR_HITLER_DEPUTIES);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
