package bo.game.interrogation;

import bo.game.Deck;

public class InterrogationDeck extends Deck<InterrogationCard> {

    public InterrogationDeck(){
        add(new InterrogationCard("0000",
                InterrogationEffect.INCREASE_SUSPICION_BY_1_OF_HIGH_OR_LOWER_SUSPICION_PLAYER,
                InterrogationEffect.DECREASE_MOTIVATION_BY_1_OF_SKEPTICAL_OR_HIGHER_MOTIVATION_PLAYER,
                InterrogationEffect.FORCE_PLAYER_TO_DISCARD_2_CARDS));

        add(new InterrogationCard("0001",
                InterrogationEffect.INCREASE_MILITARY_SUPPORT_BY_2,
                InterrogationEffect.MOVE_CLOSEST_DEPUTIES_TO_PLAYERS,
                InterrogationEffect.INCREASE_ALL_SUSPICION_BY_1_IF_ONE_CONSPIRATOR_IS_AT_HIGH_OR_LOWER_SUSPICION));

        add(new InterrogationCard("0002",
                InterrogationEffect.INCREASE_SUSPICION_BY_2_OF_PLAYER_WITH_LOW_OR_MED_SUSPICION,
                InterrogationEffect.DECREASE_MOTIVATION_BY_2_OF_TIMID_OR_SKEPTICAL_PLAYER,
                InterrogationEffect.FORCE_PLAYER_TO_DISCARD_3_CARDS));

        add(new InterrogationCard("0003",
                InterrogationEffect.RESOLVE_2_EVENT_CARDS,
                InterrogationEffect.SET_EXTREME_SUSPICION_OF_HIGH_MED_LOW_PLAYER,
                InterrogationEffect.FORCE_PLAYER_TO_DISCARD_2_TILES));

        add(new InterrogationCard("0004",
                InterrogationEffect.CLEAR_DISSENT_TRACK_IF_2_DICE,
                InterrogationEffect.REMOVE_3_ITEM_TILES_FROM_BOARD,
                InterrogationEffect.FORCE_PLAYER_TO_DISCARD_1_PLOT));

        add(new InterrogationCard("0005",
                InterrogationEffect.IMMEDIATELY_ARREST_PLAYER,
                InterrogationEffect.REMOVE_ALL_ITEMS_FROM_STAGE1_STAGE2_LOCATIONS,
                InterrogationEffect.FORCE_DISCARD_ALL_RESTRICTED_CARDS));

        shuffle();
    }
}
