package bo.game.location;

import bo.game.NaziMember;
import bo.game.item.ItemType;
import bo.game.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    public static final List<LocationName> LOCATIONS_NO_ITEM = Arrays.asList(LocationName.TRAIN_STATION, LocationName.ZURICH, LocationName.STOCKHOLM, LocationName.AUSCHWITZ, LocationName.TREBLINKA, LocationName.PARIS, LocationName.PRISON);

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

        getLocation(LocationName.CHANCELLERY).setFortified(true);
        getLocation(LocationName.WOLFSSCHLUCHT).setFortified(true);
        getLocation(LocationName.ADLERHORST).setFortified(true);
        getLocation(LocationName.TANNENBERG).setFortified(true);
        getLocation(LocationName.WOLFSSCHANZE).setFortified(true);
        getLocation(LocationName.ANLAGE_SUD).setFortified(true);
        getLocation(LocationName.WEHRWOLF).setFortified(true);
        getLocation(LocationName.SMOLENSK).setFortified(true);
        getLocation(LocationName.WASSERBURG).setFortified(true);

        getLocation(LocationName.DEUTSCHLANDHALLE).setDelivery(ItemType.MAP, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.CHANCELLERY).setDelivery(ItemType.WEAPONS, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.GESTAPO_HQ).setDelivery(ItemType.INTEL, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.ZEUGHAUS).setDelivery(ItemType.KEYS, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.SPORTPALAST).setDelivery(ItemType.BADGE, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.MINISTRY_OF_PROPOGANDA).setDelivery(ItemType.SIGNATURE, LocationModifier.SUSPICION_MINUS_2);

        getLocation(LocationName.HANNOVER).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_DEPUTY_PRESENT);
        getLocation(LocationName.ADLERHORST).setDelivery(ItemType.EXPLOSIVES, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.WOLFSSCHLUCHT).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_ABWEHR);
        getLocation(LocationName.NUREMBERG).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_HITLER_PRESENT);
        getLocation(LocationName.TANNENBERG).setDelivery(ItemType.POISON, LocationModifier.SUSPICION_MINUS_2);
        getLocation(LocationName.MUNICH).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_STAGE_1);
        getLocation(LocationName.BERGHOF).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_HITLER_PRESENT);
        getLocation(LocationName.VIENNA).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_STAGE_2);
        getLocation(LocationName.PRAGUE).setDelivery(ItemType.SIGNATURE, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.POSEN).setDelivery(ItemType.BADGE, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.WARSAW).setDelivery(ItemType.INTEL, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.ANLAGE_SUD).setDelivery(ItemType.POISON, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.WOLFSSCHANZE).setDelivery(ItemType.KEYS, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.BORISOV).setDelivery(ItemType.MAP, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.SMOLENSK).setDelivery(ItemType.EXPLOSIVES, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.WASSERBURG).setDelivery(ItemType.WEAPONS, LocationModifier.SUSPICION_MINUS_3_DISTRIBUTED);
        getLocation(LocationName.RIGA).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_CIVILIAN);
        getLocation(LocationName.WEHRWOLF).setDelivery(ItemType.ANY, LocationModifier.SUSPICION_MINUS_2_IF_WEHRMACHT);

        getLocation(LocationName.PARIS).setLocationModifier(LocationModifier.SUSPICION_MINUS_3_MILITARY_SUPPORT_PLUS_1);
        getLocation(LocationName.ZURICH).setLocationModifier(LocationModifier.SUSPICION_PLUS_1);
        getLocation(LocationName.STOCKHOLM).setLocationModifier(LocationModifier.SUSPICION_PLUS_1);
        getLocation(LocationName.AUSCHWITZ).setLocationModifier(LocationModifier.MOTIVATION_PLUS_2_SUSPICION_PLUS_1);
        getLocation(LocationName.TREBLINKA).setLocationModifier(LocationModifier.MOTIVATION_PLUS_2_SUSPICION_PLUS_1);

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

        addConnection(LocationName.GESTAPO_HQ, LocationName.PRISON);
    }

    public void move(NaziMember naziMember, LocationName destination){
        Location dest = getLocation(destination);
        move(naziMember, dest);
    }

    public void move(NaziMember naziMember, Location destination){
        Location location = getLocationWith(naziMember);
        if (location == null)
            return;
        location.getNaziMembers().remove(naziMember);
        destination.getNaziMembers().add(naziMember);
    }

    public void move(Player player, LocationName destination){
        Location location = getLocationWith(player);
        location.getPlayers().remove(player);
        Location dest = getLocation(destination);
        dest.getPlayers().add(player);
    }

    /**
     * Get the locations closest to the given location that have conspirators
     * @param location
     * @return
     */
    public List<Location> getLocationsWithConspiratorsClosestTo(Location location, boolean includeCurrentLocation){
        List<Location> returnList = new ArrayList<>();

        if (includeCurrentLocation && !location.getPlayers().isEmpty()){
            returnList.add(location);
        }
        else {
            List<Location> locationsWithPlayers = locations.values().stream().filter(loc -> !loc.getPlayers().isEmpty()).collect(Collectors.toList());
            int shortestDistance = 99;
            for (Location locationWithPlayers: locationsWithPlayers){
                List<Location> path = AStarAlgorithm.findShortestPath(location, locationWithPlayers, this);
                if (path.size() < shortestDistance){
                    returnList.clear();
                    returnList.add(locationWithPlayers);
                    shortestDistance = path.size();
                }
                else if (path.size() == shortestDistance){
                    returnList.add(locationWithPlayers);
                }
            }
        }

        return returnList;
    }

    public Map<LocationName, Location> getLocations() {
        return locations;
    }

    public Location getLocation(LocationName locationName){
        return locations.get(locationName);
    }

    public Location getLocationWith(NaziMember naziMember){
        Optional<Location> loc = locations.values().stream().filter(location -> location.getNaziMembers().contains(naziMember)).findFirst();
        if (loc.isPresent())
            return loc.get();
        return null;
    }

    public Location getLocationWith(Player player){
        return locations.values().stream().filter(location -> location.getPlayers().contains(player)).findFirst().get();
    }

    public List<Location> getBerlinLocations(){
        return locations.values().stream().filter(location -> LocationName.inBerlin(location.getName())).collect(Collectors.toList());
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
