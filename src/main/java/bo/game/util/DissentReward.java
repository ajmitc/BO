package bo.game.util;

public enum DissentReward {
    RAISE_MOTIVATION("Raise Motivation of Conspirator by 1"),
    LOWER_MILITARY_SUPPORT("Lower Military Support by 1");

    private String desc;
    DissentReward(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String toString(){
        return desc;
    }
}
