package bo.game.player;

public enum Suspicion {
    LOW,
    MEDIUM,
    HIGH,
    EXTREME;

    public Suspicion lower(){
        if (this == LOW)
            return this;
        return Suspicion.values()[ordinal() - 1];
    }

    public Suspicion raise(){
        if (this == EXTREME)
            return this;
        return Suspicion.values()[ordinal() + 1];
    }
}
