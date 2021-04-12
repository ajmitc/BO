package bo.game.location;

import bo.game.item.Item;
import bo.game.item.ItemType;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;

import java.util.*;

public class Board {
    public static final List<LocationName> LOCATIONS_NO_ITEM = Arrays.asList(LocationName.TRAIN_STATION, LocationName.ZURICH, LocationName.STOCKHOLM, LocationName.AUSCHWITZ, LocationName.TREBLINKA, LocationName.PARIS);

    private Map<LocationName, Location> locations = new HashMap<>();
    private Map<LocationName, Set<LocationName>> connections = new HashMap<>();

    public Board(){
        for (LocationName name: LocationName.values())
            locations.put(name, new Location(name));

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
    }

    public Map<LocationName, Location> getLocations() {
        return locations;
    }

    public Location getLocation(LocationName locationName){
        return locations.get(locationName);
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
