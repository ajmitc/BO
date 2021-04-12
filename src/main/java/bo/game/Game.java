package bo.game;

import bo.game.conspirator.ConspiratorDeck;
import bo.game.event.EventCardDeck;
import bo.game.interrogation.InterrogationDeck;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Board;
import bo.game.location.LocationName;
import bo.game.player.Player;

import java.util.*;

public class Game {
    private Difficulty difficulty;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private int militarySupport;
    private ConspiratorDeck conspiratorDeck;
    private EventCardDeck eventCardDeck;
    private InterrogationDeck interrogationDeck;

    public Game(){
        difficulty = Difficulty.STANDARD;
        board = new Board();
        militarySupport = difficulty.getStartingMilitarySupport();
        conspiratorDeck = new ConspiratorDeck();
        eventCardDeck = new EventCardDeck();
        interrogationDeck = new InterrogationDeck();

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
        board.getLocations().values().stream()
                .filter(location -> !Board.LOCATIONS_NO_ITEM.contains(location.getName()))
                .forEach(location -> location.setItem(items.remove(0)));

        // Put Nazi leaders at starting locations
        board.getLocation(LocationName.DEUTSCHLANDHALLE).getNaziMembers().add(NaziMember.HILTER);
        board.getLocation(LocationName.MUNICH).getNaziMembers().add(NaziMember.GOERING);
        board.getLocation(LocationName.HANNOVER).getNaziMembers().add(NaziMember.HESS);
        board.getLocation(LocationName.NUREMBERG).getNaziMembers().add(NaziMember.BORMANN);
        board.getLocation(LocationName.MINISTRY_OF_PROPOGANDA).getNaziMembers().add(NaziMember.GOEBBELS);
        board.getLocation(LocationName.GESTAPO_HQ).getNaziMembers().add(NaziMember.HIMMLER);
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

    public Board getBoard() {
        return board;
    }
}
