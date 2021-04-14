package bo.game;

import bo.Model;
import bo.game.conspirator.ConspiratorCard;
import bo.game.conspirator.ConspiratorCardType;
import bo.game.event.EventCard;
import bo.game.item.Item;
import bo.game.item.ItemType;
import bo.game.location.Location;
import bo.game.location.LocationName;
import bo.game.player.Motivation;
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
                handleBlombergFritschAffair();
                break;
            case ANSCHLUSS:
                handleTheAnschluss();
                break;
            case STRATEGY_SESSION_A:
                handleStrategySessionA();
                break;
            case STRATEGY_SESSION_B:
                handleStrategySessionB();
                break;
            case VISIT_FROM_HESS:
                handleVisitFromHessStage2();
                break;
            case GESTAPO_RAID:
                handleGestapoRaid();
                break;
            case ANTISEMETIC_LAWS:
                handleAntiSemeticLaws();
                break;
            case VISIT_FROM_BORMANN_2:
                handleVisitFromBormannStage2();
                break;
            case INVASION_OF_POLAND:
                handleInvasionOfPoland();
                break;
            case NAZI_SOVIET_PACT:
                handleNaziSovietPact();
                break;
            case PARTY_RALLY_2:
                handlePartyRallyStage2();
                break;
            case SUDETENLAND_OCCUPATION:
                handleSudetenlandOccupation();
                break;
            case CZECH_INVADED:
                handleCzechInvaded();
                break;
            case VISIT_FROM_HITLER_3:
                handleVisitFromHitlerStage3();
                break;
            case BERGHOF_RETREAT_3:
                handleBerghofRetreatStage3();
                break;
            case VISIT_FROM_GOERING_3:
                handleVisitFromGoeringStage3();
                break;
            case JEWS_TARGETED:
                handleJewsTargeted();
                break;
            case LONDON_BOMBED:
                handleLondonBombed();
                break;
            case GERMANY_BOMBED:
                handleGermanyBombed();
                break;
            case SEALION_POSTPONED:
                handleSeaLionPostponed();
                break;
            case WESTERN_OFFENSIVE:
                handleWesternOffensive();
                break;
            case INVASION_OF_NORWAY:
                handleInvasionOfNorway();
                break;
            case FALL_OF_PARIS:
                handleFallOfParis();
                break;
            case VISIT_FROM_HIMMLER_4:
                handleVisitFromHimmlerStage4();
                break;
            case WAR_CONFERENCE:
                handleWarConference();
                break;
            case HESS_LEAVES_GERMANY:
                handleHessLeavesGermany();
                break;
            case VISIT_FROM_GOEBBELS_4:
                handleVisitFromGoebbelsStage4();
                break;
            case AFRIKA_KORPS:
                handleAfrikaKorps();
                break;
            case YUGOSLAVIA_SURRENDERS:
                handleYugoslaviaSurrenders();
                break;
            case PEARL_HARBOR_ATTACK:
                handlePearlHarborAttack();
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
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.GOEBBELS);

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
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.HIMMLER);

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

    private void handleBlombergFritschAffair(){
        // Decrease Military Support by 1
        model.getGame().adjMilitarySupport(-1);
        // Move Goering to Chancellery
        model.getGame().getBoard().move(NaziMember.GOERING, LocationName.CHANCELLERY);
    }

    private void handleTheAnschluss(){
        // Increase Military Support by 1
        model.getGame().adjMilitarySupport(1);
        // Move Hitler and Goering to Munich
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.MUNICH);
        model.getGame().getBoard().move(NaziMember.GOERING, LocationName.MUNICH);
    }

    private void handleStrategySessionA(){
        model.getGame().getBoard().move(NaziMember.HIMMLER, LocationName.ADLERHORST);
        model.getGame().getBoard().move(NaziMember.GOERING, LocationName.ADLERHORST);
        model.getGame().getBoard().move(NaziMember.HESS, LocationName.ADLERHORST);
    }

    private void handleStrategySessionB(){
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.MUNICH);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.MUNICH);
        // Reveal tile in Munich, if any
        if (model.getGame().getBoard().getLocation(LocationName.MUNICH).getItem() != null)
            model.getGame().getBoard().getLocation(LocationName.MUNICH).getItem().setRevealed(true);
    }

    private void handleVisitFromHessStage2(){
        // Move Hess to closest conspirator
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.HESS);

        // If conspirator is alone, he may decrease motivation by 1 to move to any legal space and lower suspicion by 1
        if (destination.getPlayers().size() == 1){
            if (ViewUtil.popupConfirm("Visit From Hess",
                    "Do you want to decrease " + destination.getPlayers().get(0).getName() + "'s motivation by 1 to move to any legal space and lower suspicion by 1?")){
                destination.getPlayers().get(0).setMotivation(destination.getPlayers().get(0).getMotivation().lower());
                // TODO Allow player to move to any legal location
                destination.getPlayers().get(0).setSuspicion(destination.getPlayers().get(0).getSuspicion().lower());
            }
        }
    }

    private void handleGestapoRaid(){
        // Conspirators with Extreme suspicion are arrested
        model.getGame().getPlayers().stream().filter(player -> player.getSuspicion() == Suspicion.EXTREME).forEach(player -> {
            player.setArrested(true);
            model.getGame().getBoard().move(player, LocationName.JAIL);
        });
        // Conspirators who are NOT arrested may discard any number of cards
        model.getGame().getPlayers().stream().filter(player -> !player.isArrested()).forEach(player -> {
            // TODO Ask if they want to discard any cards
        });
        // Raise each conspirator's suspicion by 1 for each Restricted card they hold
        model.getGame().getPlayers().stream().forEach(player -> {
            int numRestricted = (int) player.getDossier().stream().filter(card -> card.getType() == ConspiratorCardType.RESTRICTED).count();
            for (int i = 0; i < numRestricted; ++i)
                player.setSuspicion(player.getSuspicion().raise());
        });
        // Return all dice on the Dissent track to the supply
        model.getGame().setDissentTrackDice(0);
    }

    private void handleAntiSemeticLaws(){
        model.getGame().getPlayers().stream().forEach(player -> {
            player.setMotivation(player.getMotivation().raise());
        });
    }

    private void handleVisitFromBormannStage2(){
        // Move Hess to closest conspirator
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.BORMANN);

        // If conspirator is alone, he may discard a card or item tile to lower suspicion by 2
        if (destination.getPlayers().size() == 1){
            if (ViewUtil.popupConfirm("Visit From Bormann",
                    "Do you want " + destination.getPlayers().get(0).getName() + " to discard a card or item to lower suspicion by 2?")){
                String choice = (String) ViewUtil.popupDropdown("Visit From Bormann", "Discard what?", new String[]{"Card", "Item"});
                if (choice.equals("Card")){
                    // Ask player which card to discard
                    ConspiratorCard card = (ConspiratorCard) ViewUtil.popupDropdown("Visit From Bormann", "Discard which card?", destination.getPlayers().get(0).getDossier().toArray(new ConspiratorCard[0]));
                    destination.getPlayers().get(0).getDossier().remove(card);
                }
                else {
                    // Ask player which item to discard
                    Item item = (Item) ViewUtil.popupDropdown("Visit From Bormann", "Discard which item?", destination.getPlayers().get(0).getItems().toArray(new Item[0]));
                    destination.getPlayers().get(0).getItems().remove(item);
                }
                destination.getPlayers().get(0).setSuspicion(destination.getPlayers().get(0).getSuspicion().lower().lower());
            }
        }
    }

    private void handleInvasionOfPoland(){
        model.getGame().adjMilitarySupport(2);
    }

    private void handleNaziSovietPact(){
        model.getGame().adjMilitarySupport(1);
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.BERGHOF);
        model.getGame().getBoard().move(NaziMember.HESS, LocationName.BERGHOF);
    }

    private void handlePartyRallyStage2(){
        model.getGame().resetMilitarySupport();
        Arrays.stream(NaziMember.values()).forEach(naziMember -> model.getGame().getBoard().move(naziMember, LocationName.NUREMBERG));
        // TODO While this is the current event, all conspirators that pass through Nuremberg set their suspicion to LOW
    }

    private void handleSudetenlandOccupation(){
        model.getGame().adjMilitarySupport(1);
    }

    private void handleCzechInvaded(){
        model.getGame().adjMilitarySupport(1);
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.VIENNA);

        if (model.getGame().getBoard().getLocation(LocationName.VIENNA).getItem() != null)
            model.getGame().getBoard().getLocation(LocationName.VIENNA).getItem().setRevealed(true);
    }

    private void handleVisitFromHitlerStage3(){
        // Move Hitler to closest conspirator
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.HITLER);

        // If conspirator is alone, he may reduce both motivation and suspicion to starting levels
        if (destination.getPlayers().size() == 1){
            if (ViewUtil.popupConfirm("Visit From Hitler",
                    "Do you want " + destination.getPlayers().get(0).getName() + " to reduce motivation and suspicion to starting levels?")){
                destination.getPlayers().get(0).setSuspicion(Suspicion.MEDIUM);
                destination.getPlayers().get(0).setMotivation(Motivation.TIMID);
            }
        }
    }

    private void handleBerghofRetreatStage3(){
        model.getGame().getBoard().move(NaziMember.HITLER, LocationName.BERGHOF);
        model.getGame().getBoard().move(NaziMember.HIMMLER, LocationName.BERGHOF);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.BERGHOF);
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.BERGHOF);
    }

    private void handleJewsTargeted(){
        model.getGame().getPlayers().stream().forEach(player -> {
            player.setMotivation(player.getMotivation().raise());
        });

        model.getGame().getBoard().move(NaziMember.HIMMLER, LocationName.WARSAW);
        if (model.getGame().getBoard().getLocation(LocationName.WARSAW).getItem() != null)
            model.getGame().getBoard().getLocation(LocationName.WARSAW).getItem().setRevealed(true);
    }

    private void handleVisitFromGoeringStage3(){
        // Move Goering to closest conspirator
        Location destination = moveNaziMemberToClosestConspirator(NaziMember.GOERING);

        // If conspirator is alone, he may reduce both motivation and suspicion to starting levels
        if (destination.getPlayers().size() == 1){
            while (true) {
                Item[] items = new Item[destination.getPlayers().get(0).getItems().size() + 1];
                items[0] = new Item(null);
                System.arraycopy(destination.getPlayers().get(0).getItems().toArray(), 0, items, 1, destination.getPlayers().get(0).getItems().size());
                Item item = (Item) ViewUtil.popupDropdown("Visit From Goering",
                        destination.getPlayers().get(0).getName() + " may discard an item to lower suspicion by 1 (repeatable)?",
                        items);
                if (item.getType() == null)
                    break;
                destination.getPlayers().get(0).getItems().remove(item);
                destination.getPlayers().get(0).setSuspicion(destination.getPlayers().get(0).getSuspicion().lower());
            }
        }
    }

    private void handleLondonBombed(){
        model.getGame().adjMilitarySupport(1);
        model.getGame().getBoard().move(NaziMember.HESS, LocationName.PRAGUE);
        if (model.getGame().getBoard().getLocation(LocationName.PRAGUE).getItem() != null)
            model.getGame().getBoard().getLocation(LocationName.PRAGUE).getItem().setRevealed(true);
    }

    private void handleGermanyBombed(){
        model.getGame().adjMilitarySupport(1);

        // TODO While this is the current event, add one eagle to plot attempts
    }

    private void handleSeaLionPostponed(){
        model.getGame().getPlayers().stream().forEach(player -> {
            player.setMotivation(player.getMotivation().lower());
        });
    }

    private void handleWesternOffensive(){
        model.getGame().getPlayers().stream()
                .filter(player -> player.getType() == PlayerType.WEHRMACHT)
                .forEach(player -> {
                    model.getGame().getBoard().move(player, LocationName.ADLERHORST);
                });
    }

    private void handleInvasionOfNorway(){
        model.getGame().adjMilitarySupport(1);
    }

    private void handleFallOfParis(){
        if (model.getGame().getMilitarySupport() < 5)
            model.getGame().setMilitarySupport(5);
        model.getGame().getPlayers().stream().forEach(player -> {
            player.setMotivation(player.getMotivation().lower());
        });
    }

    private void handleVisitFromHimmlerStage4(){
        // Move Himmler to closest conspirator
        moveNaziMemberToClosestConspirator(NaziMember.HIMMLER);

        model.getGame().getPlayers().stream()
                .filter(player -> player.getType() == PlayerType.WEHRMACHT)
                .forEach(player -> {
                    player.setMotivation(player.getMotivation().lower());
                });
    }

    private void handleWarConference(){
        Arrays.stream(NaziMember.values()).forEach(naziMember -> {
            model.getGame().getBoard().move(naziMember, LocationName.ANLAGE_SUD);
        });

        if (model.getGame().getBoard().getLocation(LocationName.ANLAGE_SUD).getItem() != null)
            model.getGame().getBoard().getLocation(LocationName.ANLAGE_SUD).getItem().setRevealed(true);
    }

    private void handleHessLeavesGermany(){
        model.getGame().adjMilitarySupport(-2);
        Location hessLocation = model.getGame().getBoard().getLocationWith(NaziMember.HESS);
        hessLocation.getNaziMembers().remove(NaziMember.HESS);
        model.getGame().setHessTokenOnBoard(false);
        // TODO While this is the current event, ignore one eagle on plot attempts
    }

    private void handleVisitFromGoebbelsStage4(){
        moveNaziMemberToClosestConspirator(NaziMember.GOEBBELS);

        model.getGame().getPlayers().stream()
                .filter(player -> player.getType() == PlayerType.CIVILIAN)
                .forEach(player -> {
                    player.setMotivation(player.getMotivation().lower());
                });
    }




    private Location moveNaziMemberToClosestConspirator(NaziMember naziMember){
        Location location = model.getGame().getBoard().getLocationWith(naziMember);
        List<Location> locationsWithClosestConspirators = model.getGame().getBoard().getLocationsWithConspiratorsClosestTo(location, false);
        Location destination = null;
        if (locationsWithClosestConspirators.size() == 1){
            destination = locationsWithClosestConspirators.get(0);
        }
        else {
            // Ask user to choose location
            destination = (Location) ViewUtil.popupDropdown("Visit From " + naziMember.getName(), "Choose location for " + naziMember.getName(), locationsWithClosestConspirators.toArray(new Location[0]));
        }
        model.getGame().getBoard().move(naziMember, destination);
        return destination;
    }

    private void handleAfrikaKorps(){
        model.getGame().adjMilitarySupport(1);
        model.getGame().getBoard().move(NaziMember.GOEBBELS, LocationName.PRAGUE);
        model.getGame().getBoard().move(NaziMember.BORMANN, LocationName.PRAGUE);
    }

    private void handleYugoslaviaSurrenders(){

    }

    private void handlePearlHarborAttack(){

    }
}
