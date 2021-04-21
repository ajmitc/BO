package bo.game.location;

import bo.game.NaziMember;
import bo.game.item.ItemType;
import bo.game.player.Player;
import bo.game.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private LocationName name;
    private Item item;
    private List<NaziMember> naziMembers = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private LocationModifier locationModifier;

    // Item and modifier for delivers (after initial Item is removed)
    private ItemType deliveryItem;
    private LocationModifier deliveryModifier;

    // Minimum Stage to access this location
    private int minStage = 1;
    // Maximum stage to access this location.  If game in later stage, cannot go here
    private int maxStage = 7;

    private boolean fortified = false;

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

    public LocationModifier getLocationModifier() {
        return locationModifier;
    }

    public void setLocationModifier(LocationModifier locationModifier) {
        this.locationModifier = locationModifier;
    }

    public ItemType getDeliveryItem() {
        return deliveryItem;
    }

    public void setDeliveryItem(ItemType deliveryItem) {
        this.deliveryItem = deliveryItem;
    }

    public LocationModifier getDeliveryModifier() {
        return deliveryModifier;
    }

    public void setDeliveryModifier(LocationModifier deliveryModifier) {
        this.deliveryModifier = deliveryModifier;
    }

    public void setDelivery(ItemType itemType, LocationModifier modifier){
        setDeliveryItem(itemType);
        setDeliveryModifier(modifier);
    }

    public int getMinStage() {
        return minStage;
    }

    public void setMinStage(int minStage) {
        this.minStage = minStage;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public void setMaxStage(int maxStage) {
        this.maxStage = maxStage;
    }

    public void setValidStages(int minStage, int maxStage){
        setMinStage(minStage);
        setMaxStage(maxStage);
    }

    public boolean isFortified() {
        return fortified;
    }

    public void setFortified(boolean fortified) {
        this.fortified = fortified;
    }
}
