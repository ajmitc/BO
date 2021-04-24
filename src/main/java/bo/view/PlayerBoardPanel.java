package bo.view;

import bo.Model;
import bo.game.conspirator.ConspiratorCard;
import bo.game.conspirator.ConspiratorCardType;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.player.Player;
import bo.view.util.ImageUtil;
import bo.view.util.ViewUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PlayerBoardPanel extends JPanel {
    public static final int PLAYER_BOARD_WIDTH = 605;

    private static final int BORDER_WIDTH = 2;
    private static final Stroke BORDER_STROKE = new BasicStroke(BORDER_WIDTH);
    private static final Color BORDER_COLOR = Color.RED;
    private static final int CARD_WIDTH = 100;
    private static final int ITEM_WIDTH = 35;

    private static Map<String, BufferedImage> PLAYER_BOARD_IMAGE_MAP = new HashMap<>();
    private static Map<ItemType, BufferedImage> ITEM_TOKEN_MAP = new HashMap<>();
    private static List<Point> ITEM_POINTS = new ArrayList<>();
    private static Point CARD_POINT = new Point(406, 0);
    private static List<Point> SUSPICION_POINTS = new ArrayList<>();
    private static List<Point> MOTIVATION_POINTS = new ArrayList<>();

    static {
        PLAYER_BOARD_IMAGE_MAP.put(Player.BECK,         ImageUtil.get("PlayerBoard-Beck.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.BONHOEFFER,   ImageUtil.get("PlayerBoard-Bonhoeffer.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.CANARIS,      ImageUtil.get("PlayerBoard-Canaris.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.GOERDELER,    ImageUtil.get("PlayerBoard-Goerdeler.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.KORDT,        ImageUtil.get("PlayerBoard-Kordt.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.OLBRICHT,     ImageUtil.get("PlayerBoard-Olbricht.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.OSTER,        ImageUtil.get("PlayerBoard-Oster.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.STAUFFENBERG, ImageUtil.get("PlayerBoard-Stauffenberg.jpg", PLAYER_BOARD_WIDTH));
        PLAYER_BOARD_IMAGE_MAP.put(Player.TRESCKOW,     ImageUtil.get("PlayerBoard-Tresckow.jpg", PLAYER_BOARD_WIDTH));

        ITEM_TOKEN_MAP.put(ItemType.BADGE,      ImageUtil.get("token-item-badge.png", ITEM_WIDTH, "player-item-badge"));
        ITEM_TOKEN_MAP.put(ItemType.EXPLOSIVES, ImageUtil.get("token-item-explosives.png", ITEM_WIDTH, "player-item-exp"));
        ITEM_TOKEN_MAP.put(ItemType.INTEL,      ImageUtil.get("token-item-intel.png", ITEM_WIDTH, "player-item-intel"));
        ITEM_TOKEN_MAP.put(ItemType.KEYS,       ImageUtil.get("token-item-keys.png", ITEM_WIDTH, "player-item-keys"));
        ITEM_TOKEN_MAP.put(ItemType.MAP,        ImageUtil.get("token-item-map.png", ITEM_WIDTH, "player-item-map"));
        ITEM_TOKEN_MAP.put(ItemType.POISON,     ImageUtil.get("token-item-poison.png", ITEM_WIDTH, "player-item-poison"));
        ITEM_TOKEN_MAP.put(ItemType.SIGNATURE,  ImageUtil.get("token-item-signature.png", ITEM_WIDTH, "player-item-sign"));
        ITEM_TOKEN_MAP.put(ItemType.WEAPONS,    ImageUtil.get("token-item-weapons.png", ITEM_WIDTH, "player-item-weapons"));

        ITEM_POINTS.add(new Point(36, 179));
        ITEM_POINTS.add(new Point(91, 179));
        ITEM_POINTS.add(new Point(36, 231));
        ITEM_POINTS.add(new Point(91, 231));

        MOTIVATION_POINTS.add(new Point(147, 183));  // Timid
        MOTIVATION_POINTS.add(new Point(147, 163));
        MOTIVATION_POINTS.add(new Point(147, 143));
        MOTIVATION_POINTS.add(new Point(147, 123));
        MOTIVATION_POINTS.add(new Point(147, 103));  // Zealous

        SUSPICION_POINTS.add(new Point(177, 220));   // Low
        SUSPICION_POINTS.add(new Point(232, 220));   // Medium
        SUSPICION_POINTS.add(new Point(280, 220));   // High
        SUSPICION_POINTS.add(new Point(325, 220));   // Extreme
    }

    private Model model;
    private View view;
    private Player player;
    private BufferedImage playerStatusToken;

    private int mx, my;

    // Number of columns to draw cards
    private int numCols = 2;
    private int cardHeight = 0;

    public PlayerBoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
        playerStatusToken = ImageUtil.get("cube-player-status.png", 20);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mx = e.getX();
                my = e.getY();
                refresh();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3){
                    ConspiratorCard card = getConspiratorCardAt(e.getX(), e.getY());
                    if (card != null){
                        if (card.getType() == ConspiratorCardType.PLOT)
                            view.getGamePanel().getBoardPanel().setCenterScreenImage(ImageUtil.rotateImageByDegrees(ImageUtil.get(ViewUtil.getConspiratorCardImageName(card)), 90));
                        else
                            view.getGamePanel().getBoardPanel().setCenterScreenImage(ImageUtil.get(ViewUtil.getConspiratorCardImageName(card)));
                        view.refresh();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == MouseEvent.BUTTON3){
                    view.getGamePanel().getBoardPanel().setCenterScreenImage(null);
                    view.refresh();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        if (player == null)
            return;

        g.drawImage(PLAYER_BOARD_IMAGE_MAP.get(player.getName()), 0, 0, null);

        Point p = MOTIVATION_POINTS.get(player.getMotivation().ordinal());
        g.drawImage(playerStatusToken, p.x, p.y, null);

        p = SUSPICION_POINTS.get(player.getSuspicion().ordinal());
        g.drawImage(playerStatusToken, p.x, p.y, null);

        // Draw Items
        for (int i = 0; i < player.getItems().size(); ++i){
            Item item = player.getItems().get(i);
            g.drawImage(ITEM_TOKEN_MAP.get(item.getType()), ITEM_POINTS.get(i).x, ITEM_POINTS.get(i).y, null);
        }

        // Draw dossier
        for (int i = 0; i < player.getDossier().size(); ++i){
            ConspiratorCard card = player.getDossier().get(i);
            int row = i / numCols;
            int col = i % numCols;
            BufferedImage cardImageSmall = ImageUtil.get(ViewUtil.getConspiratorCardImageName(card), CARD_WIDTH, "cc-" + card.getId() + "-small");
            g.drawImage(cardImageSmall, CARD_POINT.x + (col * CARD_WIDTH), CARD_POINT.y + (row * cardImageSmall.getHeight()), null);
            cardHeight = cardImageSmall.getHeight();
        }

        if (model.getGame().getCurrentPlayer() == player){
            Stroke oldStroke = g.getStroke();
            g.setColor(BORDER_COLOR);
            g.setStroke(BORDER_STROKE);
            g.drawRect(0, 0, getWidth() - BORDER_WIDTH, getHeight() - BORDER_WIDTH);
            g.setStroke(oldStroke);
        }

        g.setColor(Color.WHITE);
        g.drawString(mx + ", " + my, 20, 20);
    }

    public void refresh(){
        repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ConspiratorCard getConspiratorCardAt(int mx, int my){
        if (cardHeight > 0 && mx >= CARD_POINT.x) {
            int row = (my - CARD_POINT.y) / cardHeight;
            int col = (mx - CARD_POINT.x) / CARD_WIDTH;
            int index = (row * numCols) + col;
            if (index >= 0 && index < player.getDossier().size())
                return player.getDossier().get(index);
        }
        return null;
    }
}
