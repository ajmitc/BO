package bo.game.location;

import bo.game.NaziMember;
import bo.game.player.Player;
import bo.game.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private LocationName name;
    private Item item;
    private List<NaziMember> naziMembers = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    // Minimum Stage to access this location
    private int minStage = 0;

    public Location(LocationName name){
        this.name = name;
    }

    public Location(LocationName name, int stage){
        this.name = name;
        this.minStage = stage;
    }

    public LocationName getName() {
        return name;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<NaziMember> getNaziMembers() {
        return naziMembers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMinStage() {
        return minStage;
    }

    public void setMinStage(int minStage) {
        this.minStage = minStage;
    }
}
