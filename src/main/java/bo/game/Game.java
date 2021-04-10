package bo.game;

import bo.game.conspirator.ConspiratorDeck;
import bo.game.event.EventCardDeck;
import bo.game.interrogation.InterrogationDeck;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.location.LocationName;
import bo.game.player.Player;

import java.util.*;

public class Game {
    private static final List<LocationName> LOCATIONS_NO_ITEM = Arrays.asList(LocationName.TRAIN_STATION, LocationName.ZURICH, LocationName.STOCKHOLM, LocationName.AUSCHWITZ, LocationName.TREBLINKA, LocationName.PARIS);

    private Difficulty difficulty;
    private Map<LocationName, Location> locations = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private int militarySupport;
    private ConspiratorDeck conspiratorDeck;
    private EventCardDeck eventCardDeck;
    private InterrogationDeck interrogationDeck;

    public Game(){
        difficulty = Difficulty.STANDARD;
        militarySupport = difficulty.getStartingMilitarySupport();
        conspiratorDeck = new ConspiratorDeck();
        eventCardDeck = new EventCardDeck();
        interrogationDeck = new InterrogationDeck();

        for (LocationName name: LocationName.values())
            locations.put(name, new Location(name));

        // Create items and distribute to locations
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.BADGE));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.EXPLOSIVES));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.INTEL));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.KEYS));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.MAP));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.POISON));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.SIGNATURE));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.WEAPONS));
        Collections.shuffle(items);
        locations.values().stream()
                .filter(location -> !LOCATIONS_NO_ITEM.contains(location.getName()))
                .forEach(location -> location.setItem(items.remove(0)));

        // Put Nazi leaders at starting locations
        locations.get(LocationName.DEUTSCHLANDHALLE).getNaziMembers().add(NaziMember.HILTER);
        locations.get(LocationName.MUNICH).getNaziMembers().add(NaziMember.GOERING);
        locations.get(LocationName.HANNOVER).getNaziMembers().add(NaziMember.HESS);
        locations.get(LocationName.NUREMBERG).getNaziMembers().add(NaziMember.BORMANN);
        locations.get(LocationName.MINISTRY_OF_PROPOGANDA).getNaziMembers().add(NaziMember.GOEBBELS);
        locations.get(LocationName.GESTAPO_HQ).getNaziMembers().add(NaziMember.HIMMLER);


        players.add(new Player(Player.BECK));
        players.add(new Player(Player.OSTER));
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.militarySupport = difficulty.getStartingMilitarySupport();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<LocationName, Location> getLocations() {
        return locations;
    }

    public Location getLocation(LocationName name){
        return locations.get(name);
    }

    public int getMilitarySupport() {
        return militarySupport;
    }

    public void setMilitarySupport(int militarySupport) {
        this.militarySupport = militarySupport;
    }

    public void adjMilitarySupport(int amount){
        this.militarySupport += amount;
    }

    public ConspiratorDeck getConspiratorDeck() {
        return conspiratorDeck;
    }

    public EventCardDeck getEventCardDeck() {
        return eventCardDeck;
    }

    public InterrogationDeck getInterrogationDeck() {
        return interrogationDeck;
    }
}
