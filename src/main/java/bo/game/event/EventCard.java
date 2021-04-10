package bo.game.event;

public class EventCard {
    private EventCardType type;
    private String name;
    private int stage;  // 1-7
    private String id;  // Match event with card image
    private EventCardEffect effect;

    public EventCard(EventCardType type, String name, int stage, String id, EventCardEffect effect){
        this.type = type;
        this.name = name;
        this.stage = stage;
        this.id = id;
        this.effect = effect;
    }

    public EventCardType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getStage() {
        return stage;
    }

    public String getId() {
        return id;
    }

    public EventCardEffect getEffect() {
        return effect;
    }
}
