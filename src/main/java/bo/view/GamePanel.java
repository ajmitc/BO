package bo.view;

import bo.Model;
import bo.game.item.ItemType;
import bo.view.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    private static final Map<ItemType, BufferedImage> ITEM_TOKEN_IMAGES = new HashMap<>();
    public static final int ITEM_TOKEN_WIDTH = 56;

    static {
        ITEM_TOKEN_IMAGES.put(ItemType.BADGE,      ImageUtil.get("token-item-badge.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.EXPLOSIVES, ImageUtil.get("token-item-explosives.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.INTEL,      ImageUtil.get("token-item-intel.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.KEYS,       ImageUtil.get("token-item-keys.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.MAP,        ImageUtil.get("token-item-map.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.POISON,     ImageUtil.get("token-item-poison.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.SIGNATURE,  ImageUtil.get("token-item-signature.png", ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_IMAGES.put(ItemType.WEAPONS,    ImageUtil.get("token-item-weapons.png", ITEM_TOKEN_WIDTH));
    }

    private BoardPanel boardPanel;
    private PlayerBoardsPanel playerBoardsPanel;
    private ActionPanel actionPanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());

        boardPanel        = new BoardPanel(model, view);
        playerBoardsPanel = new PlayerBoardsPanel(model, view);
        actionPanel       = new ActionPanel(model, view);

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(playerBoardsPanel, BorderLayout.CENTER);
        eastPanel.add(actionPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    public void refresh(){
        boardPanel.refresh();
    }

    public BufferedImage getItemImage(ItemType type){
        return ITEM_TOKEN_IMAGES.get(type);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public PlayerBoardsPanel getPlayerBoardsPanel() {
        return playerBoardsPanel;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }
}
