package bo.game.plot;

import bo.game.Game;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.player.Motivation;
import bo.game.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Plot {
    private String id;
    private List<PlotFeature> plotFeatures = new ArrayList<>();

    public Plot(String id){
        this.id = id;
    }

    public boolean requirementsMet(Game game){
        return game.getCurrentPlayer().getMotivation().ordinal() >= Motivation.COMMITTED.ordinal();
    }

    public int getNumFreeExtraDice(Game game){
        return 0;
    }

    public Map<ItemType, List<Player>> getItemsAvailableForExtraDice(Game game){
        return new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public List<PlotFeature> getPlotFeatures() {
        return plotFeatures;
    }
}
