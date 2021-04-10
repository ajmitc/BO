package bo.game.interrogation;

public class InterrogationCard {
    private String id;
    private InterrogationEffect[] effects;

    public InterrogationCard(String id, InterrogationEffect ... effects){
        this.id = id;
        this.effects = new InterrogationEffect[3];
        for (int i = 0; i < 3; ++i){
            this.effects[i] = effects[i];
        }
    }

    public String getId() {
        return id;
    }

    public InterrogationEffect[] getEffects() {
        return effects;
    }
}
