package bo.view;

import bo.Model;
import bo.game.player.Player;
import bo.view.util.ImageUtil;

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
    private static Map<String, BufferedImage> PLAYER_BOARD_IMAGE_MAP = new HashMap<>();
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

    private Player player;
    private BufferedImage playerStatusToken;

    private int mx, my;

    public PlayerBoardPanel(Model model, View view){
        super();
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
}
