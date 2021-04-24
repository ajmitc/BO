package bo.game.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        return DIE_VALUES[GEN.nextInt(6)];
    }

    public static List<DieResult> roll(int numDice){
        List<DieResult> results = new ArrayList<>();
        for (int i = 0; i < numDice; ++i)
            results.add(roll());
        return results;
    }

    public static int randInt(int min, int max){
        return min + GEN.nextInt(max - min);
    }

    public static int randInt(int max){
        return randInt(0, max);
    }

    private Util(){}
}
