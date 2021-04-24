package bo;

import bo.game.player.Player;

public interface CommonController {
    void handleFullDissentTrack();
    void drawConspiratorCard(Player player);
    void checkNumItems(Player player);
}
