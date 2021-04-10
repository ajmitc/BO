package bo.game;

public enum Difficulty {
    EASY(2),
    STANDARD(3),
    HARD(4);

    private int startingMilitarySupport;
    Difficulty(int startingMilitarySupport){
        this.startingMilitarySupport = startingMilitarySupport;
    }

    public int getStartingMilitarySupport() {
        return startingMilitarySupport;
    }
}
