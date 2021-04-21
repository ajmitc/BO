package bo.game.conspirator;

public class ConspiratorCard {
    private ConspiratorCardType type;
    private String name;
    private String id;
    private ConspiratorCardEffect effect;
    private ConspiratorCardPlayTiming timing;

    public ConspiratorCard(ConspiratorCardType type, String name, String id, ConspiratorCardEffect effect, ConspiratorCardPlayTiming timing){
        this.type = type;
        this.name = name;
        this.id = id;
        this.effect = effect;
        this.timing = timing;
    }

    public ConspiratorCardType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ConspiratorCardEffect getEffect() {
        return effect;
    }

    public ConspiratorCardPlayTiming getTiming() {
        return timing;
    }

    public String toString(){
        return this.name;
    }
}
