package bo.game.player;

public enum PlayerSpecialAbility {
    // Beck
    ADD_DIE_TO_PLOT_IN_SAME_LOCATION,
    HITLER_AND_DEPUTY_TOKENS_NO_EFFECT,

    // Bonhoeffer
    ACTION_INCREASE_MOTIVATION_OF_OTHER_CONSPIRATORS_SAME_LOCATION, // Once per turn

    // Canaris
    ACTION_DISCARD_ITEM_TO_LOWER_SUSPICION_OF_ANY_PLAYER, // including yourself

    // Goerdeler
    DRAW_1_CONSPIRATOR_CARD_AT_START_OF_TURN,    // Not an action
    HAND_LIMIT_PLUS_2,

    // Kordt
    ACTION_REARRANGE_TOP_THREE_EVENT_CARDS,

    // Olbricht
    ACTION_MOVE_2_SPACES_OR_MOVE_ANOTHER_CONSPIRATOR_1_SPACE,

    // Oster
    ACTION_DRAW_2_CARDS_KEEP_ONE_DISCARD_OTHER, // whenever player takes Draw A Card action

    // Stauffenberg
    TAKE_4_ACTIONS,

    // Tresckow
    HOLD_PLUS_1_ITEM,
    ACTION_PICK_UP_UNREVEALED_ITEM_SAME_LOCATION,
    ACTION_REVEAL_ANY_ITEM_TILE
}
