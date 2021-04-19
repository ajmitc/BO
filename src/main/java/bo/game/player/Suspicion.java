package bo.game.player;

public enum Suspicion {
    LOW(3),
    MEDIUM(2),
    HIGH(1),
    EXTREME(1);

    private int numEaglesForPlotFailure;
    Suspicion(int numEaglesForPlotFailure){
        this.numEaglesForPlotFailure = numEaglesForPlotFailure;
    }

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

    public int getNumEaglesForPlotFailure() {
        return numEaglesForPlotFailure;
    }
}
