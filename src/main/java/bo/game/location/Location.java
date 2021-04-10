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

    public Location(LocationName name){
        this.name = name;
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
}
