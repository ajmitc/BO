package bo.game.interrogation;

public enum InterrogationEffect {
    TRY_AND_RESIST,
    INCREASE_SUSPICION_BY_1_OF_HIGH_OR_LOWER_SUSPICION_PLAYER,
    DECREASE_MOTIVATION_BY_1_OF_SKEPTICAL_OR_HIGHER_MOTIVATION_PLAYER,
    FORCE_PLAYER_TO_DISCARD_2_CARDS,  // must choose player with at least 2 cards
    INCREASE_MILITARY_SUPPORT_BY_2,
    MOVE_CLOSEST_DEPUTIES_TO_PLAYERS, // Not Hitler
    INCREASE_ALL_SUSPICION_BY_1_IF_ONE_CONSPIRATOR_IS_AT_HIGH_OR_LOWER_SUSPICION,
    INCREASE_SUSPICION_BY_2_OF_PLAYER_WITH_LOW_OR_MED_SUSPICION,
    DECREASE_MOTIVATION_BY_2_OF_TIMID_OR_SKEPTICAL_PLAYER,
    FORCE_PLAYER_TO_DISCARD_3_CARDS, // must choose player with at least 3 cards
    RESOLVE_2_EVENT_CARDS, // at end of this turn
    SET_EXTREME_SUSPICION_OF_HIGH_MED_LOW_PLAYER,
    FORCE_PLAYER_TO_DISCARD_2_TILES, // must choose player with at least 2 tiles
    CLEAR_DISSENT_TRACK_IF_2_DICE,
    REMOVE_3_ITEM_TILES_FROM_BOARD, // if there are <3 items, cannot choose this option
    FORCE_PLAYER_TO_DISCARD_1_PLOT, // player must have at least 1 plot card
    IMMEDIATELY_ARREST_PLAYER,  // player cannot already be in jail
    REMOVE_ALL_ITEMS_FROM_STAGE1_STAGE2_LOCATIONS,
    FORCE_DISCARD_ALL_RESTRICTED_CARDS // if < 2 total restricted cards amongst players NOT in jail, cannot choose this option
}
