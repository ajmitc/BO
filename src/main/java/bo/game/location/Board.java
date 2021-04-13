package bo.game.location;

import bo.game.item.Item;
import bo.game.item.ItemType;

import java.util.*;

public class Board {
    public static final List<LocationName> LOCATIONS_NO_ITEM = Arrays.asList(LocationName.TRAIN_STATION, LocationName.ZURICH, LocationName.STOCKHOLM, LocationName.AUSCHWITZ, LocationName.TREBLINKA, LocationName.PARIS);

    private Map<LocationName, Location> locations = new HashMap<>();
    private Map<LocationName, Set<LocationName>> connections = new HashMap<>();

    public Board(){
        for (LocationName name: LocationName.values())
            locations.put(name, new Location(name));

        getLocation(LocationName.BERGHOF).setMinStage(2);
        getLocation(LocationName.VIENNA).setMinStage(2);
        getLocation(LocationName.ZURICH).setMinStage(3);
        getLocation(LocationName.PRAGUE).setMinStage(3);
        getLocation(LocationName.POSEN).setMinStage(3);
        getLocation(LocationName.WARSAW).setValidStages(3, 6);
        getLocation(LocationName.WOLFSSCHANZE).setValidStages(3, 6);
        getLocation(LocationName.ANLAGE_SUD).setValidStages(3, 6);
        getLocation(LocationName.WOLFSSCHLUCHT).setValidStages(4, 6);
        getLocation(LocationName.PARIS).setValidStages(4, 6);
        getLocation(LocationName.AUSCHWITZ).setValidStages(4, 6);
        getLocation(LocationName.TREBLINKA).setValidStages(4, 6);
        getLocation(LocationName.STOCKHOLM).setValidStages(4, 6);
        getLocation(LocationName.RIGA).setValidStages(4, 6);
        getLocation(LocationName.WASSERBURG).setValidStages(5, 6);
        getLocation(LocationName.BORISOV).setValidStages(5, 6);
        getLocation(LocationName.SMOLENSK).setValidStages(5, 6);
        getLocation(LocationName.WEHRWOLF).setValidStages(5, 6);

        locations.keySet().stream().forEach(name -> connections.put(name, new HashSet<>()));
        connectAll(LocationName.DEUTSCHLANDHALLE, LocationName.MINISTRY_OF_PROPOGANDA, LocationName.CHANCELLERY, LocationName.GESTAPO_HQ, LocationName.ZEUGHAUS, LocationName.SPORTPALAST, LocationName.TRAIN_STATION);
        addConnections(LocationName.TRAIN_STATION, LocationName.HANNOVER, LocationName.NUREMBERG, LocationName.PRAGUE, LocationName.POSEN);
        addConnections(LocationName.WOLFSSCHLUCHT, LocationName.ADLERHORST, LocationName.PARIS);
        addConnections(LocationName.TANNENBERG, LocationName.ADLERHORST, LocationName.MUNICH);
        addConnections(LocationName.MUNICH, LocationName.TANNENBERG, LocationName.ZURICH, LocationName.BERGHOF, LocationName.VIENNA);
        addConnections(LocationName.NUREMBERG, LocationName.ADLERHORST, LocationName.MUNICH, LocationName.PRAGUE);
        addConnections(LocationName.ANLAGE_SUD, LocationName.AUSCHWITZ, LocationName.WARSAW, LocationName.VIENNA, LocationName.WEHRWOLF);
        addConnection(LocationName.PRAGUE, LocationName.AUSCHWITZ);
        addConnections(LocationName.WARSAW, LocationName.ANLAGE_SUD, LocationName.POSEN, LocationName.TREBLINKA, LocationName.WOLFSSCHANZE);
        addConnections(LocationName.WOLFSSCHANZE, LocationName.RIGA, LocationName.BORISOV, LocationName.POSEN);
        addConnections(LocationName.STOCKHOLM, LocationName.RIGA);
        addConnections(LocationName.WASSERBURG, LocationName.RIGA, LocationName.BORISOV);
        addConnections(LocationName.SMOLENSK, LocationName.BORISOV);

        addConnection(LocationName.GESTAPO_HQ, LocationName.JAIL);
    }

    public Map<LocationName, Location> getLocations() {
        return locations;
    }

    public Location getLocation(LocationName locationName){
        return locations.get(locationName);
    }

    public Set<LocationName> getConnections(LocationName locationName){
        return connections.get(locationName);
    }

    private void addConnection(LocationName loc1, LocationName loc2){
        connections.get(loc1).add(loc2);
        connections.get(loc2).add(loc1);
    }

    private void addConnections(LocationName locationName, LocationName ... locationNames){
        for (LocationName loc: locationNames){
            addConnection(locationName, loc);
        }
    }

    private void connectAll(LocationName ... locationNames){
        for (LocationName locationName: locationNames){
            for (LocationName locationName1: locationNames){
                if (locationName != locationName1)
                    continue;
                addConnection(locationName, locationName1);
            }
        }
    }
}
