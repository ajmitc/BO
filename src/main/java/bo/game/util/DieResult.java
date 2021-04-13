package bo.game.util;

public enum DieResult {
    EAGLE(0),
    TARGET(0),
    ONE(1),
    TWO(2),
    THREE(3);

    private int value;
    DieResult(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
