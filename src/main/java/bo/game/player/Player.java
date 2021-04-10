package bo.game.player;

import bo.game.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public static final String BECK         = "Beck";
    public static final String BONHOEFFER   = "Bonhoeffer";
    public static final String CANARIS      = "Canaris";
    public static final String GOERDELER    = "Goerdeler";
    public static final String KORDT        = "Kordt";
    public static final String OLBRICHT     = "Olbricht";
    public static final String OSTER        = "Oster";
    public static final String STAUFFENBERG = "Stauffenberg";
    public static final String TRESCKOW     = "Tresckow";

    private PlayerType type;
    private String name; // Character name (ie. Beck, Oster, etc)
    private List<Item> items = new ArrayList<>();
    private Motivation motivation;
    private Suspicion suspicion;

    public Player(String name){
        this.name = name;
        this.motivation = Motivation.TIMID;
        this.suspicion = Suspicion.MEDIUM;

        if (name.equals(BONHOEFFER) || name.equals(CANARIS) || name.equals(OSTER))
            type = PlayerType.ABWEHR;
        else if (name.equals(GOERDELER) || name.equals(KORDT))
            type = PlayerType.CIVILIAN;
        else if (name.equals(BECK) || name.equals(OLBRICHT) || name.equals(STAUFFENBERG) || name.equals(TRESCKOW))
            type = PlayerType.WEHRMACHT;
    }

    public String getName() {
        return name;
    }

    public PlayerType getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public Motivation getMotivation() {
        return motivation;
    }

    public void setMotivation(Motivation motivation) {
        this.motivation = motivation;
    }

    public Suspicion getSuspicion() {
        return suspicion;
    }

    public void setSuspicion(Suspicion suspicion) {
        this.suspicion = suspicion;
    }
}
