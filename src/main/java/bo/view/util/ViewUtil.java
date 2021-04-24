package bo.view.util;

import bo.game.conspirator.ConspiratorCard;
import bo.game.event.EventCard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ViewUtil {
    public static final String FONT_NAME = "Serif";
    public static final Font DEFAULT_FONT = new Font(FONT_NAME, Font.PLAIN, 12);
    public static final Font TITLE_FONT = new Font(FONT_NAME, Font.BOLD, 14);

    public static void popupNotify(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static void popupNotify(String title, BufferedImage bufferedImage){
        JOptionPane.showMessageDialog(null, null, title, JOptionPane.PLAIN_MESSAGE, new ImageIcon(bufferedImage));
    }

    public static boolean popupConfirm(String title, String message){
        int ret = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return ret >= 1;
    }

    public static Object popupDropdown(String title, String message, Object[] options){
        Object selected = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return selected;
    }

    public static Color toColor(String name){
        if (name.equalsIgnoreCase("blue"))
            return Color.BLUE;
        if (name.equalsIgnoreCase("red"))
            return Color.RED;
        return Color.BLACK;
    }

    public static String getConspiratorCardImageName(ConspiratorCard card){
        return "card-conspirator-" + card.getType().name().substring(0, 1).toUpperCase() + card.getType().name().substring(1).toLowerCase() + "-_" + card.getId() + ".jpg";
    }

    public static String getEventCardImageName(EventCard eventCard){
        return "Event-_" + eventCard.getId() + "_" + eventCard.getStage() + ".jpg";
    }

    private ViewUtil(){}
}
