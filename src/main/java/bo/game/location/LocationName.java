package bo.game.location;

import java.util.Arrays;
import java.util.List;

public enum LocationName {
    // BERLIN
    DEUTSCHLANDHALLE,
    CHANCELLERY,
    GESTAPO_HQ,
    ZEUGHAUS,
    MINISTRY_OF_PROPOGANDA,
    SPORTPALAST,
    JAIL,      // Not a real location, but Players may be here

    TRAIN_STATION,

    // OUTSIDE BERLIN
    HANNOVER,
    WOLFSSCHLUCHT,
    PARIS,
    ADLERHORST,
    NUREMBERG,
    TANNENBERG,
    MUNICH,
    ZURICH,
    BERGHOF,
    VIENNA,
    PRAGUE,
    POSEN,
    WARSAW,
    AUSCHWITZ,
    ANLAGE_SUD,
    WEHRWOLF,
    TREBLINKA,
    WOLFSSCHANZE,
    BORISOV,
    SMOLENSK,
    RIGA,
    WASSERBURG,
    STOCKHOLM;

    private static final List<LocationName> BERLIN_LOCATIONS =
            Arrays.asList(DEUTSCHLANDHALLE,
                    CHANCELLERY,
                    GESTAPO_HQ,
                    ZEUGHAUS,
                    MINISTRY_OF_PROPOGANDA,
                    SPORTPALAST);

    public static boolean inBerlin(LocationName locationName){
        return BERLIN_LOCATIONS.contains(locationName);
    }
}
