package bo.game;

import bo.Model;
import bo.game.event.EventCard;
import bo.game.location.Location;
import bo.game.location.LocationName;
import bo.game.player.PlayerType;
import bo.game.player.Suspicion;
import bo.game.util.DieResult;
import bo.game.util.Util;
import bo.view.View;
import bo.view.util.ViewUtil;

import java.util.Arrays;
import java.util.List;

public class EventResolver {
    private Model model;
    private View view;

    public EventResolver(Model model, View view){
        this.model = model;
        this.view = view;
    }

    public void resolveEvent(EventCard eventCard){
        switch (eventCard.getEffect()){
            case PARADE_IN_BERLIN:
                handleParadeInBerlin();
                break;
            case VISIT_FROM_GOEBBELS_1:
                handleVisitFromGoebbelsStage1();
                break;
            case PARTY_RALLY_1:
                handlePartyRallyStage1();
                break;
            case LEADERSHIP_DISPUTE:
                handleLeadershipDispute();
                break;
            case VISIT_FROM_HIMMLER_1:
                handleVisitFromHimmlerStage1();
                break;
            case KRISTALLNACHT:
                handleKristallnacht();
                break;
            case BERLIN_SPEECH:
                handleBerlinSpeech();
                break;
            case ASSASSIN_CAUGHT:
                handleAssassinCaught();
                break;
            case GESTAPO_INVESTIGATION:
                handleGestapoInvestigation();
                break;
            case MUNICH_AGREEMENT:
                handleMunichAgreement();
                break;
            case BLOMBERG_FRITSCH_AFFAIR:
                break;
            case ANSCHLUSS:
                break;
            case STRATEGY_SESSION_A:
                break;
            case STRATEGY_SESSION_B:
                break;
            case VISIT_FROM_HESS:
                break;
            case GESTAPO_RAID:
                break;
            case ANTISEMETIC_LAWS:
                break;
            case VISIT_FROM_BORMANN_2:
                break;
            case INVASION_OF_POLAND:
                break;
            case NAZI_SOVIET_PACT:
                break;
            case PARTY_RALLY_2:
                break;
            case SUDETENLAND_OCCUPATION:
                break;
            case CZECH_INVADED:
                break;
            case VISIT_FROM_HITLER_3:
                break;
            case BERGHOF_RETREAT_3:
                break;
            case VISIT_FROM_GOERING_3:
                break;
            case JEWS_TARGETED:
                break;
            case LONDON_BOMBED:
                break;
            case GERMANY_BOMBED:
                break;
            case SEALION_POSTPONED:
                break;
            case WESTERN_OFFENSIVE:
                break;
            case INVASION_OF_NORWAY:
                break;
            case FALL_OF_PARIS:
                break;
            case VISIT_FROM_HIMMLER_4:
                break;
            case WAR_CONFERENCE:
                break;
            case HESS_LEAVES_GERMANY:
                break;
            case VISIT_FROM_GOEBBELS_4:
                break;
            case AFRIKA_KORPS:
                break;
            case YUGOSLAVIA_SURRENDERS:
                break;
            case PEARL_HARBOR_ATTACK:
                break;
            case FALL_OF_KIEV:
                break;
            case OPERATION_BARBAROSSA:
                break;
            case HITLER_ASSUMES_COMMAND:
                break;
            case VISIT_FROM_BORMANN_5:
                break;
            case VISIT_FROM_GOERING_5:
                break;
            case CASABLANCA_CONFERENCE:
                break;
            case HEYDRICH_ASSASSINATED:
                break;
            case BERGHOF_RETREAT_5:
                break;
            case ROMMEL_DEFEATED:
                break;
            case RUSSIAN_SETBACK:
                break;
            case VISIT_FRONT:
                break;
            case EASTERN_PUSH:
                break;
            case LEBENSRAUM:
                break;
            case VISIT_FROM_HITLER_6:
                break;
            case SLOW_RETREAT:
                break;
            case BERLIN_BOMBED:
                break;
            case WARSAW_GHETTO_UPRISING:
                break;
            case STALINGRAD_SURROUNDED:
                break;
            case WOLFPACKS:
                break;
            case SICILY_INVADED:
                break;
            case FORTRESS_EUROPA:
                break;
            case KURSK_RECAPTURED:
                break;
            case V2_ROCKET:
                break;
            case FUHRER_BUNKER:
                break;
            case OPERATION_OVERLORD:
                break;
            case GOTHIC_LINE:
                break;
            case ROMMEL_EXECUTED:
                break;
            case DOCUMENTS_LOCATED:
                break;
            case FALL_OF_ROME:
                break;
            case FINAL_CONSCRIPTION:
                break;
            case ANZIO_LANDING:
                break;
        }
    }

    private void handleParadeInBerlin(){
        // Move Hitler, Goebbels, and Hess to Zeughaus
        model.getGame().getBoard().move(NaziMember.HITLER,   LocationName.ZEUGHAUS);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.ZEUGHAUS);
        model.getGame().getBoard().move(NaziMember.HESS,     LocationName.ZEUGHAUS);

        // All Wehrmacht in Berlin lower suspicion by 1
        model.getGame().getBoard().getBerlinLocations().stream().forEach(location -> {
            location.getPlayers().stream()
                    .filter(player -> player.getType() == PlayerType.WEHRMACHT)
                    .forEach(player -> player.setSuspicion(player.getSuspicion().lower()));
        });

        // TODO While this is the current event, ignore 2 eagles on all plot attempts

        view.refresh();
    }

    private void handleVisitFromGoebbelsStage1(){
        // Move Goebbels to location with nearest conspirator(s)
        Location goebbelsLocation = model.getGame().getBoard().getLocationWith(NaziMember.GOEBBELS);
        List<Location> locationsWithClosestConspirators = model.getGame().getBoard().getLocationsWithConspiratorsClosestTo(goebbelsLocation, false);
        Location destination = null;
        if (locationsWithClosestConspirators.size() == 1){
            destination = locationsWithClosestConspirators.get(0);
        }
        else {
            // Ask user to choose location
            destination = (Location) ViewUtil.popupDropdown("Visit From Goebbels", "Choose location for Goebbels", locationsWithClosestConspirators.toArray(new Location[0]));
        }
        model.getGame().getBoard().move(NaziMember.GOEBBELS, destination);

        if (destination.getPlayers().size() == 1){
            // If that conspirator is alone, he may raise his Suspicion to HIGH to increase Motivation by 2
            if (ViewUtil.popupConfirm("Visit From Goebbels",
                    "Do you want to increase " + destination.getPlayers().get(0).getName() + "'s suspicion to HIGH to increase his motivation by 2?")){
                destination.getPlayers().get(0).setSuspicion(Suspicion.HIGH);
                destination.getPlayers().get(0).setMotivation(destination.getPlayers().get(0).getMotivation().raise().raise());
            }
        }
    }

    private void handlePartyRallyStage1(){
        // Move Hitler and all deputies to Nuremberg
        Arrays.stream(NaziMember.values()).forEach(naziMember -> model.getGame().getBoard().move(naziMember, LocationName.NUREMBERG));
    }

    private void handleLeadershipDispute(){
        // Move Bormann to Hannover
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.HANNOVER);
        // Move Goering to Tannenberg
        model.getGame().getBoard().move(NaziMember.GOERING, LocationName.TANNENBERG);
        // Move Himmler to Nuremburg
        model.getGame().getBoard().move(NaziMember.HIMMLER, LocationName.NUREMBERG);
    }

    private void handleVisitFromHimmlerStage1(){
        // Move Himmler to closest conspirator
        Location himmlerLocation = model.getGame().getBoard().getLocationWith(NaziMember.HIMMLER);
        List<Location> locationsWithClosestConspirators = model.getGame().getBoard().getLocationsWithConspiratorsClosestTo(himmlerLocation, false);
        Location destination = null;
        if (locationsWithClosestConspirators.size() == 1){
            destination = locationsWithClosestConspirators.get(0);
        }
        else {
            // Ask user to choose location
            destination = (Location) ViewUtil.popupDropdown("Visit From Himmler", "Choose location for Himmler", locationsWithClosestConspirators.toArray(new Location[0]));
        }
        model.getGame().getBoard().move(NaziMember.HIMMLER, destination);

        if (destination.getPlayers().size() == 1){
            // If that conspirator is alone, he may roll a die.  If he rolls a number, increase motivation by that amount.  If he rolls an eagle, raise his suspicion by 2
            if (ViewUtil.popupConfirm("Visit From Himmler", "Do you want to roll a die?")){
                DieResult dieResult = Util.roll();
                if (dieResult.getValue() > 0){
                    for (int i = 0; i < dieResult.getValue(); ++i)
                        destination.getPlayers().get(0).setMotivation(destination.getPlayers().get(0).getMotivation().raise());
                    ViewUtil.popupNotify("Motivation increased by " + dieResult.getValue());
                }
                else if (dieResult == DieResult.EAGLE){
                    destination.getPlayers().get(0).setSuspicion(destination.getPlayers().get(0).getSuspicion().raise().raise());
                    ViewUtil.popupNotify("Suspicion increased by 2");
                }
            }
        }
    }

    private void handleKristallnacht(){
        // Increase all conspirator's motivation by 1
        model.getGame().getPlayers().stream().forEach(player -> player.setMotivation(player.getMotivation().raise()));
        // Move hitler and deputies to starting locations
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.DEUTSCHLANDHALLE);
        model.getGame().getBoard().move(NaziMember.GOERING, LocationName.MUNICH);
        model.getGame().getBoard().move(NaziMember.HESS, LocationName.HANNOVER);
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.NUREMBERG);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.MINISTRY_OF_PROPOGANDA);
        model.getGame().getBoard().move(NaziMember.HIMMLER, LocationName.GESTAPO_HQ);
    }

    private void handleBerlinSpeech(){
        // Set Military Support to starting value
        model.getGame().resetMilitarySupport();
        // Move hitler and Goebbels to SPORTPALAST
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.SPORTPALAST);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.SPORTPALAST);
        // Lower suspicion of any conspirator in Berlin by 1
        model.getGame().getBoard().getBerlinLocations().stream().forEach(location -> {
            location.getPlayers().stream().forEach(player -> {
                player.setSuspicion(player.getSuspicion().lower());
            });
        });
    }

    private void handleAssassinCaught(){
        // Conspirators closest to Hitler raise Suspicion to HIGH
        Location hitlerLocation = model.getGame().getBoard().getLocationWith(NaziMember.HITLER);
        List<Location> locationsWithClosestConspirators = model.getGame().getBoard().getLocationsWithConspiratorsClosestTo(hitlerLocation, true);
        locationsWithClosestConspirators.stream().forEach(location -> {
            location.getPlayers().stream().forEach(player -> {
                player.setSuspicion(Suspicion.HIGH);
            });
        });

        // Move Hitler to Chancellery
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.CHANCELLERY);
    }

    private void handleGestapoInvestigation(){
        // Raise all conspirator's suspicion by 1
        model.getGame().getPlayers().stream().forEach(player -> {
            player.setSuspicion(player.getSuspicion().raise());
        });
    }

    private void handleMunichAgreement(){
        // Increase Military Support by 1
        model.getGame().setMilitarySupport(model.getGame().getMilitarySupport() + 1);
        // Remove next two Stage 1 Event cards from game
        Deck<EventCard> deck = model.getGame().getEventCardDeck().getStageDeck(1);
        deck.draw();
        deck.draw();
    }
}
