package bo.game.plot;

import bo.game.Game;
import bo.game.NaziMember;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.player.Motivation;
import bo.game.player.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<Item> getItemsAvailableForExtraDice(Game game) {
        List<Item> items = new ArrayList<>();
        Optional<Item> itemOptional = game.getCurrentPlayer().getItems().stream().filter(item -> item.getType() == ItemType.WEAPONS).findFirst();
        if (itemOptional.isPresent())
            items.add(itemOptional.get());
        else {
            // TODO If any players in same location have weapons, they can be used instead
        }
        return items;
    }
}
