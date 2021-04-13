package bo.game.util;

import java.util.Date;
import java.util.Random;

public class Util {
    private static Random GEN = new Random(new Date().getTime());

    private static final DieResult[] DIE_VALUES = {
            DieResult.EAGLE,
            DieResult.TARGET,
            DieResult.TARGET,
            DieResult.ONE,
            DieResult.TWO,
            DieResult.THREE
    };

    public static DieResult roll(){
        return DIE_VALUES[GEN.nextInt(6) + 1];
    }

    private Util(){}
}
