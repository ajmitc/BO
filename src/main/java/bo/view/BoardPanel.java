package bo.view;

import bo.Model;
import bo.game.MilitarySupportLevel;
import bo.game.NaziMember;
import bo.game.item.ItemType;
import bo.game.player.Player;
import bo.game.location.Location;
import bo.game.location.LocationName;
import bo.view.util.ImageUtil;
import bo.view.util.ViewUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class BoardPanel extends JPanel {
    private static Logger logger = Logger.getLogger(BoardPanel.class.getName());

    private static final String CARDS_BOARD_IMAGE = "board-cards.jpg";
    private static final String BOARD_IMAGE = "Main_board_vassal.jpg";
    private static final String MIL_SUPPORT_IMAGE = "map-military-support.jpg";
    private static final Map<String, BufferedImage> PLAYER_TOKEN_MAP = new HashMap<>();
    private static final Map<NaziMember, BufferedImage> NAZI_TOKEN_MAP = new HashMap<>();
    private static final Map<ItemType, BufferedImage> ITEM_TOKEN_MAP = new HashMap<>();
    private static final Map<LocationName, Point> LOCATION_ITEM_POINTS = new HashMap<>();
//    private static final Map<LocationName, List<Point>> LOCATION_NAZI_POINTS = new HashMap<>();
//    private static final Map<LocationName, List<Point>> LOCATION_PLAYER_POINTS = new HashMap<>();
    private static final Map<LocationName, Point> LOCATION_POINT = new HashMap<>();
    private static final List<Point> MILITARY_SUPPORT_TOKEN_POINTS = new ArrayList<>();
    private static final List<Point> DISSENT_TRACK_POINTS = new ArrayList<>();
    private static final Point CURRENT_EVENT_POINT = new Point(1059, 345);
    private static final Point KEY_EVENT_POINT = new Point(1283, 345);

    private static final int BOARD_WIDTH = 1315;
    private static final int MIL_SUPPORT_WIDTH = 150;
    private static final int MIL_SUPPORT_XOFFSET = 945;
    private static final int MIL_SUPPORT_YOFFSET = 817;
    private static final int PLAYER_TOKEN_WIDTH = 60;
    private static final int EVENT_CARD_WIDTH = 150;

    static {
        PLAYER_TOKEN_MAP.put(Player.BECK,         ImageUtil.get("token-player-v2-beck.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.BONHOEFFER,   ImageUtil.get("token-player-v2-bonhoeffer.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.CANARIS,      ImageUtil.get("token-player-v2-canaris.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.GOERDELER,    ImageUtil.get("token-player-v2-goerdeler.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.KORDT,        ImageUtil.get("token-player-v2-kordt.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.OLBRICHT,     ImageUtil.get("token-player-v2-olbricht.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.OSTER,        ImageUtil.get("token-player-v2-oster.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.STAUFFENBERG, ImageUtil.get("token-player-v2-stauffenberg.png", PLAYER_TOKEN_WIDTH));
        PLAYER_TOKEN_MAP.put(Player.TRESCKOW,     ImageUtil.get("token-player-v2-tresckow.png", PLAYER_TOKEN_WIDTH));

        NAZI_TOKEN_MAP.put(NaziMember.HITLER,   ImageUtil.get("token-reich-hitler-v2.png", PLAYER_TOKEN_WIDTH));
        NAZI_TOKEN_MAP.put(NaziMember.BORMANN,  ImageUtil.get("token-reich-bormann-v2.png", PLAYER_TOKEN_WIDTH));
        NAZI_TOKEN_MAP.put(NaziMember.GOEBBELS, ImageUtil.get("token-reich-goebbels-v2.png", PLAYER_TOKEN_WIDTH));
        NAZI_TOKEN_MAP.put(NaziMember.GOERING,  ImageUtil.get("token-reich-goering-v2.png", PLAYER_TOKEN_WIDTH));
        NAZI_TOKEN_MAP.put(NaziMember.HESS,     ImageUtil.get("token-reich-hess-v2.png", PLAYER_TOKEN_WIDTH));
        NAZI_TOKEN_MAP.put(NaziMember.HIMMLER,  ImageUtil.get("token-reich-himmler-v2.png", PLAYER_TOKEN_WIDTH));

        ITEM_TOKEN_MAP.put(ItemType.BADGE,      ImageUtil.get("token-item-badge.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.EXPLOSIVES, ImageUtil.get("token-item-explosives.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.INTEL,      ImageUtil.get("token-item-intel.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.KEYS,       ImageUtil.get("token-item-keys.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.MAP,        ImageUtil.get("token-item-map.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.POISON,     ImageUtil.get("token-item-poison.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.SIGNATURE,  ImageUtil.get("token-item-signature.png", GamePanel.ITEM_TOKEN_WIDTH));
        ITEM_TOKEN_MAP.put(ItemType.WEAPONS,    ImageUtil.get("token-item-weapons.png", GamePanel.ITEM_TOKEN_WIDTH));

        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point()); // 0
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point()); // 1
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1000, 855)); // 2
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1046, 855)); // 3
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1094, 855)); // 4
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1142, 855)); // 5
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1190, 855)); // 6
        MILITARY_SUPPORT_TOKEN_POINTS.add(new Point(1235, 855)); // 7

        LOCATION_ITEM_POINTS.put(LocationName.DEUTSCHLANDHALLE, new Point(166, 153));
        LOCATION_ITEM_POINTS.put(LocationName.GESTAPO_HQ, new Point(209, 293));
        LOCATION_ITEM_POINTS.put(LocationName.CHANCELLERY, new Point(346, 199));
        LOCATION_ITEM_POINTS.put(LocationName.ZEUGHAUS, new Point(480, 122));
        LOCATION_ITEM_POINTS.put(LocationName.MINISTRY_OF_PROPOGANDA, new Point(503, 258));
        LOCATION_ITEM_POINTS.put(LocationName.SPORTPALAST, new Point(358, 347));

        LOCATION_ITEM_POINTS.put(LocationName.HANNOVER, new Point(343, 496));
        LOCATION_ITEM_POINTS.put(LocationName.WOLFSSCHLUCHT, new Point(130, 585));
        LOCATION_ITEM_POINTS.put(LocationName.ADLERHORST, new Point(295, 621));
        LOCATION_ITEM_POINTS.put(LocationName.TANNENBERG, new Point(316, 743));
        LOCATION_ITEM_POINTS.put(LocationName.NUREMBERG, new Point(458, 625));
        LOCATION_ITEM_POINTS.put(LocationName.MUNICH, new Point(471, 757));
        LOCATION_ITEM_POINTS.put(LocationName.BERGHOF, new Point(551, 865));
        LOCATION_ITEM_POINTS.put(LocationName.VIENNA, new Point(636, 754));
        LOCATION_ITEM_POINTS.put(LocationName.PRAGUE, new Point(604, 605));
        LOCATION_ITEM_POINTS.put(LocationName.POSEN, new Point(670, 459));
        LOCATION_ITEM_POINTS.put(LocationName.WARSAW, new Point(839, 482));
        LOCATION_ITEM_POINTS.put(LocationName.ANLAGE_SUD, new Point(942, 607));
        LOCATION_ITEM_POINTS.put(LocationName.WEHRWOLF, new Point(1200, 613));
        LOCATION_ITEM_POINTS.put(LocationName.WOLFSSCHANZE, new Point(791, 327));
        LOCATION_ITEM_POINTS.put(LocationName.BORISOV, new Point(1038, 236));
        LOCATION_ITEM_POINTS.put(LocationName.RIGA, new Point(985, 109));
        LOCATION_ITEM_POINTS.put(LocationName.WASSERBURG, new Point(1152, 60));
        LOCATION_ITEM_POINTS.put(LocationName.SMOLENSK, new Point(1220, 240));


//        Arrays.stream(LocationName.values()).forEach(locationName -> LOCATION_NAZI_POINTS.put(locationName, new ArrayList<>()));
//        LOCATION_NAZI_POINTS.get(LocationName.DEUTSCHLANDHALLE).add(new Point(103, 130));
//        LOCATION_NAZI_POINTS.get(LocationName.MUNICH).add(new Point(410, 736));
//        LOCATION_NAZI_POINTS.get(LocationName.HANNOVER).add(new Point(281, 471));
//        LOCATION_NAZI_POINTS.get(LocationName.NUREMBERG).add(new Point(397, 600));
//        LOCATION_NAZI_POINTS.get(LocationName.MINISTRY_OF_PROPOGANDA).add(new Point(442, 237));
//        LOCATION_NAZI_POINTS.get(LocationName.GESTAPO_HQ).add(new Point(146, 270));
        // TODO Add the rest of the locations


//        Arrays.stream(LocationName.values()).forEach(locationName -> LOCATION_PLAYER_POINTS.put(locationName, new ArrayList<>()));
//        LOCATION_PLAYER_POINTS.get(LocationName.SPORTPALAST).add(new Point(385, 375));
//        LOCATION_PLAYER_POINTS.get(LocationName.GESTAPO_HQ).add(new Point(238, 318));
//        LOCATION_PLAYER_POINTS.get(LocationName.TRAIN_STATION).add(new Point(496, 489));

        // Location Points (center)
        // Berlin
        LOCATION_POINT.put(LocationName.DEUTSCHLANDHALLE,       new Point(192, 179));
        LOCATION_POINT.put(LocationName.MINISTRY_OF_PROPOGANDA, new Point(530, 285));
        LOCATION_POINT.put(LocationName.SPORTPALAST,            new Point(385, 375));
        LOCATION_POINT.put(LocationName.GESTAPO_HQ,             new Point(235, 320));
        LOCATION_POINT.put(LocationName.CHANCELLERY,            new Point(372, 225));
        LOCATION_POINT.put(LocationName.ZEUGHAUS,               new Point(507, 150));
        LOCATION_POINT.put(LocationName.TRAIN_STATION,          new Point(530, 475));

        // Stage 1
        LOCATION_POINT.put(LocationName.MUNICH,                 new Point(498, 786));
        LOCATION_POINT.put(LocationName.HANNOVER,               new Point(370, 520));
        LOCATION_POINT.put(LocationName.NUREMBERG,              new Point(485, 653));
        LOCATION_POINT.put(LocationName.PRAGUE,                 new Point(630, 633));
        LOCATION_POINT.put(LocationName.POSEN,                  new Point(697, 485));
        LOCATION_POINT.put(LocationName.ADLERHORST,             new Point(323, 648));
        LOCATION_POINT.put(LocationName.TANNENBERG,             new Point(344, 772));
        // Stage 2
        LOCATION_POINT.put(LocationName.VIENNA,                 new Point(663, 782));
        LOCATION_POINT.put(LocationName.BERGHOF,                new Point(578, 894));
        // TODO Add the rest of the locations


        DISSENT_TRACK_POINTS.add(new Point(31, 63));
        DISSENT_TRACK_POINTS.add(new Point(84, 63));
        DISSENT_TRACK_POINTS.add(new Point(140, 63));
    }

    private Model model;
    private View view;

    private BufferedImage boardImage;
    private BufferedImage militarySupportImage;
    private BufferedImage itemBackImage;
    private BufferedImage militarySupportToken;
    private BufferedImage targetDieResult;
    private int dragXoffset;
    private int dragYoffset;

    private int mx, my;
    private int mxdown, mydown;

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get(BOARD_IMAGE, BOARD_WIDTH);
        militarySupportImage = ImageUtil.rotateImageByDegrees(ImageUtil.get(MIL_SUPPORT_IMAGE, MIL_SUPPORT_WIDTH), 90);
        itemBackImage = ImageUtil.get("token-item-back.png", GamePanel.ITEM_TOKEN_WIDTH);
        militarySupportToken = ImageUtil.get("cube-military.png", 30);
        targetDieResult = ImageUtil.get("0_3.png", 45);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mx = e.getX();
                my = e.getY();
                refresh();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int dx = e.getX() - mxdown;
                int dy = e.getY() - mydown;
                dragXoffset += dx;
                dragYoffset += dy;
                mxdown = e.getX();
                mydown = e.getY();
                refresh();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mxdown = e.getX();
                mydown = e.getY();
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        if (model.getGame() == null)
            return;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);

        g.drawImage(boardImage, 0, 0, null);
        g.drawImage(militarySupportImage, MIL_SUPPORT_XOFFSET, MIL_SUPPORT_YOFFSET, null);

        // Draw Military Support Token
        Point p = MILITARY_SUPPORT_TOKEN_POINTS.get(model.getGame().getMilitarySupport());
        g.drawImage(militarySupportToken, p.x, p.y, null);

        // Draw starting support level and difficulty
        g.drawImage(MilitarySupportLevel.getStartingMilitarySupportImage(model.getGame().getDifficulty().getStartingMilitarySupport()), 747, 818, null);

        for (Location location: model.getGame().getBoard().getLocations().values()){
            // Draw item
            if (location.getItem() != null){
                p = LOCATION_ITEM_POINTS.get(location.getName());
                if (location.getItem().isRevealed()) {
                    g.drawImage(ITEM_TOKEN_MAP.get(location.getItem().getType()), p.x, p.y, null);
                }
                else
                    g.drawImage(itemBackImage, p.x, p.y, null);
            }

            // Draw Nazi leaders
            for (int i = 0; i < location.getNaziMembers().size(); ++i){
                NaziMember naziMember = location.getNaziMembers().get(i);
                //p = LOCATION_NAZI_POINTS.get(location.getName()).get(i);
                p = LOCATION_POINT.get(location.getName());
                int x = p.x - 89 - (i * 15);
                int y = p.y - 50 + (i * 15);
                /*
                if (location.getName() == LocationName.GESTAPO_HQ){
                    p.x = dragXoffset;
                    p.y = dragYoffset;
                }
                 */
                //g.drawImage(NAZI_TOKEN_MAP.get(naziMember), p.x, p.y, null);
                g.drawImage(NAZI_TOKEN_MAP.get(naziMember), x, y, null);
            }

            // Draw players
            for (int i = 0; i < location.getPlayers().size(); ++i){
                Player player = location.getPlayers().get(i);
                Point point = LOCATION_POINT.get(location.getName());
                int x = point.x + 20 + (i * 15);
                int y = point.y - 50 + (i * 15);
                g.drawImage(PLAYER_TOKEN_MAP.get(player.getName()), x, y, null);
            }
        }

        // Draw Dissent Track
        for (int i = 0; i < model.getGame().getDissentTrackDice(); ++i)
            g.drawImage(targetDieResult, DISSENT_TRACK_POINTS.get(i).x, DISSENT_TRACK_POINTS.get(i).y, null);

        // Draw Current Event
        if (model.getGame().getCurrentEventCard() != null)
            g.drawImage(ImageUtil.get(ViewUtil.getEventCardImageName(model.getGame().getCurrentEventCard()), EVENT_CARD_WIDTH, "event-board-" + model.getGame().getCurrentEventCard().getId()), CURRENT_EVENT_POINT.x, CURRENT_EVENT_POINT.y, null);
        if (model.getGame().getCurrentKeyEventCard() != null)
            g.drawImage(ImageUtil.get(ViewUtil.getEventCardImageName(model.getGame().getCurrentKeyEventCard()), EVENT_CARD_WIDTH, "event-board-" + model.getGame().getCurrentKeyEventCard().getId()), KEY_EVENT_POINT.x, KEY_EVENT_POINT.y, null);

        g.setColor(Color.WHITE);
        g.drawString(mx + ", " + my, 20, 20);
    }

    public void refresh(){
        repaint();
    }
}
