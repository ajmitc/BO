package bo.game.player;

public enum Motivation {
    TIMID,
    SKEPTICAL,
    MOTIVATED,
    COMMITTED,
    RECKLESS;

    public Motivation lower(){
        if (this == TIMID)
            return TIMID;
        int ord = this.ordinal() - 1;
        return Motivation.values()[ord];
    }

    public Motivation raise(){
        if (this == RECKLESS)
            return RECKLESS;
        int ord = this.ordinal() + 1;
        return Motivation.values()[ord];
    }
}
