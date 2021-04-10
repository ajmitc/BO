package bo.game;

import bo.view.util.ImageUtil;

import java.awt.image.BufferedImage;

public class MilitarySupportLevel {
    public static BufferedImage getStartingMilitarySupportImage(int level){
        switch (level) {
            case 2: {
                return ImageUtil.get("token-MS-2-easy.jpg");
            }
            case 3: {
                return ImageUtil.get("token-MS-3-standard.jpg");
            }
            case 4: {
                return ImageUtil.get("token-MS-4-hard.jpg");
            }
        }
        return null;
    }

    public static BufferedImage getMilitarySupportImage(int level){
        switch (level) {
            case 2: {
                return ImageUtil.get("token-MS-2-easy.jpg");
            }
            case 3: {
                return ImageUtil.get("token-MS-3.jpg");
            }
            case 4: {
                return ImageUtil.get("token-MS-4.jpg");
            }
            case 5: {
                return ImageUtil.get("token-MS-5.jpg");
            }
            case 6: {
                return ImageUtil.get("token-MS-6.jpg");
            }
            case 7: {
                return ImageUtil.get("token-MS-7.jpg");
            }
        }
        return null;
    }
}
