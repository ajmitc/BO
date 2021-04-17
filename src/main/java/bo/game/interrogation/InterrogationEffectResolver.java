package bo.game.interrogation;

import bo.Model;
import bo.game.conspirator.ConspiratorCardType;
import bo.game.player.Motivation;
import bo.game.player.Player;
import bo.game.player.Suspicion;
import bo.view.View;
import bo.view.util.ViewUtil;

import java.util.List;
import java.util.stream.Collectors;

public class InterrogationEffectResolver {
    private Model model;
    private View view;

    public InterrogationEffectResolver(Model model, View view){
        this.model = model;
        this.view = view;
    }

    public boolean isFullyApplicable(InterrogationEffect effect){
        switch (effect){
            case INCREASE_SUSPICION_BY_1_OF_HIGH_OR_LOWER_SUSPICION_PLAYER:
            case INCREASE_ALL_SUSPICION_BY_1_IF_ONE_CONSPIRATOR_IS_AT_HIGH_OR_LOWER_SUSPICION:
            case SET_EXTREME_SUSPICION_OF_HIGH_MED_LOW_PLAYER:
                // Determine if there is at least one Conspirator with suspicion at HIGH or lower
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getSuspicion().ordinal() <= Suspicion.HIGH.ordinal());
            case DECREASE_MOTIVATION_BY_1_OF_SKEPTICAL_OR_HIGHER_MOTIVATION_PLAYER:
            case DECREASE_MOTIVATION_BY_2_OF_TIMID_OR_SKEPTICAL_PLAYER:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getMotivation().ordinal() >= Motivation.SKEPTICAL.ordinal());
            case FORCE_PLAYER_TO_DISCARD_2_CARDS:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getDossier().size() >= 2);
            case INCREASE_MILITARY_SUPPORT_BY_2:
                return model.getGame().getMilitarySupport() <= 5;
            case MOVE_CLOSEST_DEPUTIES_TO_PLAYERS:
            case RESOLVE_2_EVENT_CARDS:
            case IMMEDIATELY_ARREST_PLAYER:
                return true;
            case INCREASE_SUSPICION_BY_2_OF_PLAYER_WITH_LOW_OR_MED_SUSPICION:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getSuspicion().ordinal() <= Suspicion.MEDIUM.ordinal());
            case FORCE_PLAYER_TO_DISCARD_3_CARDS:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getDossier().size() >= 3);
            case FORCE_PLAYER_TO_DISCARD_2_TILES:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getItems().size() >= 2);
            case CLEAR_DISSENT_TRACK_IF_2_DICE:
                return model.getGame().getDissentTrackDice() == 2;
            case REMOVE_3_ITEM_TILES_FROM_BOARD:
                return model.getGame().getBoard().getLocations().values().stream().filter(location -> location.getItem() != null).count() >= 3;
            case FORCE_PLAYER_TO_DISCARD_1_PLOT:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getDossier().stream().anyMatch(card -> card.getType() == ConspiratorCardType.PLOT));
            case REMOVE_ALL_ITEMS_FROM_STAGE1_STAGE2_LOCATIONS:
                return model.getGame().getBoard().getLocations().values().stream().filter(location -> location.getMinStage() <= 2).anyMatch(location -> location.getItem() != null);
            case FORCE_DISCARD_ALL_RESTRICTED_CARDS:
                return model.getGame().getPlayers().stream().anyMatch(player -> player != model.getGame().getCurrentPlayer() && player.getDossier().stream().anyMatch(card -> card.getType() == ConspiratorCardType.RESTRICTED));
        }
        return false;
    }

    public void resolveEffect(InterrogationEffect effect){
        switch (effect){
            case INCREASE_SUSPICION_BY_1_OF_HIGH_OR_LOWER_SUSPICION_PLAYER: {
                // Player must choose player to increase suspicion
                List<Player> candidates =
                        model.getGame().getPlayers().stream()
                                .filter(player -> player != model.getGame().getCurrentPlayer() && player.getSuspicion().ordinal() <= Suspicion.HIGH.ordinal())
                                .collect(Collectors.toList());
                Player selected = (Player) ViewUtil.popupDropdown("Interrogation", "Choose Player to increase their suspicion", candidates.toArray(new Player[0]));
                selected.setSuspicion(selected.getSuspicion().raise());
                break;
            }
            case DECREASE_MOTIVATION_BY_1_OF_SKEPTICAL_OR_HIGHER_MOTIVATION_PLAYER: {
                // Player must choose player to decrease motivation
                List<Player> candidates =
                        model.getGame().getPlayers().stream()
                                .filter(player -> player != model.getGame().getCurrentPlayer() && player.getMotivation().ordinal() >= Motivation.SKEPTICAL.ordinal())
                                .collect(Collectors.toList());
                Player selected = (Player) ViewUtil.popupDropdown("Interrogation", "Choose Player to decease their motivation", candidates.toArray(new Player[0]));
                selected.setMotivation(selected.getMotivation().lower());
                break;
            }
            case FORCE_PLAYER_TO_DISCARD_2_CARDS: {
                List<Player> candidates =
                        model.getGame().getPlayers().stream()
                                .filter(player -> player != model.getGame().getCurrentPlayer() && player.getDossier().size() >= 2)
                                .collect(Collectors.toList());
                Player selected = (Player) ViewUtil.popupDropdown("Interrogation", "Choose Player to discard 2 cards", candidates.toArray(new Player[0]));
                // TODO Implement this
                break;
            }
            case INCREASE_MILITARY_SUPPORT_BY_2:
                model.getGame().adjMilitarySupport(2);
                break;
            case MOVE_CLOSEST_DEPUTIES_TO_PLAYERS:
                // TODO implement this
                break;
            case INCREASE_ALL_SUSPICION_BY_1_IF_ONE_CONSPIRATOR_IS_AT_HIGH_OR_LOWER_SUSPICION:
                break;
            case INCREASE_SUSPICION_BY_2_OF_PLAYER_WITH_LOW_OR_MED_SUSPICION:
                break;
            case DECREASE_MOTIVATION_BY_2_OF_TIMID_OR_SKEPTICAL_PLAYER:
                break;
            case FORCE_PLAYER_TO_DISCARD_3_CARDS:
                break;
            case RESOLVE_2_EVENT_CARDS: // at end of turn
                break;
            case SET_EXTREME_SUSPICION_OF_HIGH_MED_LOW_PLAYER:
                break;
            case FORCE_PLAYER_TO_DISCARD_2_TILES:
                break;
            case CLEAR_DISSENT_TRACK_IF_2_DICE:
                break;
            case REMOVE_3_ITEM_TILES_FROM_BOARD:
                break;
            case FORCE_PLAYER_TO_DISCARD_1_PLOT:
                break;
            case IMMEDIATELY_ARREST_PLAYER:
                break;
            case REMOVE_ALL_ITEMS_FROM_STAGE1_STAGE2_LOCATIONS:
                break;
            case FORCE_DISCARD_ALL_RESTRICTED_CARDS:
                break;
        }
    }
}
