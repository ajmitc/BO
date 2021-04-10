package bo.view.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ImageUtil {
    private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
    private static final String IMAGE_DIR = "images/";

    private static Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage get(String name, Integer w, Integer h){
        return get(name, w, h, name);
    }

    public static BufferedImage get(String filename, Integer w, Integer h, String key){
        if (cache.containsKey(key))
            return cache.get(key);
        try {
            BufferedImage bi = ImageIO.read(ImageUtil.class.getClassLoader().getResourceAsStream(IMAGE_DIR + filename));
            if (w != null || h != null){
                int iw = bi.getWidth(null);
                int ih = bi.getHeight(null);
                if (h == null){
                    double scale = ((double) w) / ((double) iw);
                    h = (int) (ih * scale);
                }
                else if (w == null){
                    double scale = h / ih;
                    w = (int) (iw * scale);
                }
                bi = toBufferedImage(bi.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH));
            }
            cache.put(key, bi);
            return bi;
        }
        catch (Exception e){
            logger.severe("Unable to load image '" + IMAGE_DIR + filename + "': " + e);
        }
        return null;
    }

    public static BufferedImage get(String name){
        return get(name, null, null, name);
    }

    public static BufferedImage get(String name, Integer w){
        return get(name, w, null, name);
    }

    public static BufferedImage get(String name, Integer w, String key){
        return get(name, w, null, key);
    }

    public static BufferedImage get(String filename, String key){
        return get(filename, null, null, key);
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public static BufferedImage scaleWidth(BufferedImage img, int newWidth){
        int iw = img.getWidth(null);
        int ih = img.getHeight(null);
        double scale = ((double) newWidth) / ((double) iw);
        int newHeight = (int) (ih * scale);
        BufferedImage bi = toBufferedImage(img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH));
        return bi;
    }

    public static int getScaledHeight(int originalWidth, int originalHeight, int newWidth){
        double scale = ((double) newWidth) / ((double) originalWidth);
        return (int) (originalHeight * scale);
    }

    private ImageUtil(){}
}
