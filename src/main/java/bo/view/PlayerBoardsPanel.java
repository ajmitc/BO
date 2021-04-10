package bo.view;

import bo.Model;
import bo.game.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Display all player boards
 */
public class PlayerBoardsPanel extends JPanel {
    private Model model;
    private View view;

    private List<PlayerBoardPanel> playerBoardPanels = new ArrayList<>();

    public PlayerBoardsPanel(Model model, View view){
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.model = model;
        this.view = view;
        setPreferredSize(new Dimension(PlayerBoardPanel.PLAYER_BOARD_WIDTH, Toolkit.getDefaultToolkit().getScreenSize().height));
    }

    public void addPlayerBoard(Player player){
        PlayerBoardPanel playerBoardPanel = new PlayerBoardPanel(model, view);
        playerBoardPanel.setPlayer(player);
        playerBoardPanels.add(playerBoardPanel);
        add(playerBoardPanel);
    }

    public void refresh(){
        for (PlayerBoardPanel playerBoardPanel: playerBoardPanels){
            playerBoardPanel.refresh();
        }
    }
}
