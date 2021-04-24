package bo.game.conspirator;

public enum ConspiratorEffectResolution {
    // Effect was cancelled or didn't meet requirements
    // No action should be used and card should not be discarded
    CANCELLED,

    // Effect was applied normally,
    // Action should be taken (if applicable) and card discarded
    DONE,

    // Effect was applied normally
    // Do not discard card (probably was already discarded within Resolver)
    DONE_NO_DISCARD
}
