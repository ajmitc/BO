package bo.game.plot;

import bo.game.Game;
import bo.game.NaziMember;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.player.Motivation;
import bo.game.player.Player;
import bo.game.player.PlayerType;

import java.util.*;

public class PlotA extends Plot{

    public PlotA(){
        super("0000_A");
        getPlotFeatures().add(PlotFeature.BADGE_BLOCKS_EAGLE);
    }

    @Override
    public boolean requirementsMet(Game game) {
        if (!super.requirementsMet(game))
            return false;
        // Must be in same space as Hitler
        Location playerLocation = game.getBoard().getLocationWith(game.getCurrentPlayer());
        Location hitlerLocation = game.getBoard().getLocationWith(NaziMember.HITLER);
        if (playerLocation.getName() != hitlerLocation.getName())
            return false;
        if (game.getCurrentPlayer().getMotivation() != Motivation.RECKLESS)
            return false;
        return true;
    }

    @Override
    public int getNumFreeExtraDice(Game game) {
        int extraDice = 0;
        if (game.getCurrentPlayer().getType() == PlayerType.WEHRMACHT)
            extraDice += 1;
        return extraDice;
    }

    @Override
    public Map<ItemType, List<Player>> getItemsAvailableForExtraDice(Game game) {
        Map<ItemType, List<Player>> items = new HashMap<>();
        Optional<Item> itemOptional = game.getCurrentPlayer().getItems().stream().filter(item -> item.getType() == ItemType.WEAPONS).findFirst();
        if (itemOptional.isPresent()) {
            items.put(ItemType.WEAPONS, new ArrayList<>());
            items.get(ItemType.WEAPONS).add(game.getCurrentPlayer());
        }
        else {
            // If any players in same location have weapons, they can be used instead
            Location location = game.getBoard().getLocationWith(game.getCurrentPlayer());
            location.getPlayers().stream()
                    .filter(player -> player != game.getCurrentPlayer())
                    .forEach(player -> {
                        Optional<Item> optItem = player.getItems().stream().filter(item -> item.getType() == ItemType.WEAPONS).findFirst();
                        if (optItem.isPresent()){
                            if (!items.containsKey(ItemType.WEAPONS))
                                items.put(ItemType.WEAPONS, new ArrayList<>());
                            items.get(ItemType.WEAPONS).add(player);
                        }
                    });
        }
        return items;
    }
}
