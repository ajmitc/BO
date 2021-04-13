package bo.game.event;

import bo.game.Deck;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the Event cards for all stages
 */
public class EventCardDeck {
    private Map<Integer, Deck<EventCard>> stageDecks = new HashMap<>();

    public EventCardDeck(){
        stageDecks.put(1, new Deck<>());
        stageDecks.put(2, new Deck<>());
        stageDecks.put(3, new Deck<>());
        stageDecks.put(4, new Deck<>());
        stageDecks.put(5, new Deck<>());
        stageDecks.put(6, new Deck<>());
        stageDecks.put(7, new Deck<>());

        // Build the decks
        // Stage 1
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Parade In Berlin", 1, "0000", EventCardEffect.PARADE_IN_BERLIN));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Visit From Goebbels", 1, "0001", EventCardEffect.VISIT_FROM_GOEBBELS_1));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Party Rally", 1, "0002", EventCardEffect.PARTY_RALLY_1));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Leadership Dispute", 1, "0003", EventCardEffect.LEADERSHIP_DISPUTE));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Visit From Himmler", 1, "0004", EventCardEffect.VISIT_FROM_HIMMLER_1));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Kristallnacht", 1, "0005", EventCardEffect.KRISTALLNACHT));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Berlin Speech", 1, "0006", EventCardEffect.BERLIN_SPEECH));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Assassin Caught", 1, "0007", EventCardEffect.ASSASSIN_CAUGHT));
        stageDecks.get(1).add(new EventCard(EventCardType.NORMAL, "Gestapo Investigation", 1, "0008", EventCardEffect.GESTAPO_INVESTIGATION));
        stageDecks.get(1).add(new EventCard(EventCardType.KEY_EVENT, "Munich Agreement", 1, "0009", EventCardEffect.MUNICH_AGREEMENT));
        stageDecks.get(1).add(new EventCard(EventCardType.IGNORABLE, "Blomberg-fritsch Affair", 1, "0010", EventCardEffect.BLOMBERG_FRITSCH_AFFAIR));
        stageDecks.get(1).add(new EventCard(EventCardType.IGNORABLE, "The Anschluss", 1, "0011", EventCardEffect.ANSCHLUSS));

        // Stage 2
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Assassin Caught", 2, "0012", EventCardEffect.ASSASSIN_CAUGHT));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Strategy Session", 2, "0013", EventCardEffect.STRATEGY_SESSION_A));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Strategy Session", 2, "0014", EventCardEffect.STRATEGY_SESSION_B));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Visit From Hess", 2, "0015", EventCardEffect.VISIT_FROM_HESS));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 2, "0016", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Anti-Semetic Laws", 2, "0017", EventCardEffect.ANTISEMETIC_LAWS));
        stageDecks.get(2).add(new EventCard(EventCardType.NORMAL, "Visit From Bormann", 2, "0018", EventCardEffect.VISIT_FROM_BORMANN_2));
        stageDecks.get(2).add(new EventCard(EventCardType.KEY_EVENT, "Invasion of Poland", 2, "0019", EventCardEffect.INVASION_OF_POLAND));
        stageDecks.get(2).add(new EventCard(EventCardType.IGNORABLE, "Nazi-Soviet Pact", 2, "0020", EventCardEffect.NAZI_SOVIET_PACT));
        stageDecks.get(2).add(new EventCard(EventCardType.IGNORABLE, "Party Rally", 2, "0021", EventCardEffect.PARTY_RALLY_2));
        stageDecks.get(2).add(new EventCard(EventCardType.IGNORABLE, "Sudetenland Occupation", 2, "0022", EventCardEffect.SUDETENLAND_OCCUPATION));
        stageDecks.get(2).add(new EventCard(EventCardType.IGNORABLE, "Czechoslovakia Invaded", 2, "0023", EventCardEffect.CZECH_INVADED));

        // Stage 3
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Visit From Hitler", 3, "0024", EventCardEffect.VISIT_FROM_HITLER_3));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Berghof Retreat", 3, "0025", EventCardEffect.BERGHOF_RETREAT_3));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Visit From Goering", 3, "0026", EventCardEffect.VISIT_FROM_GOERING_3));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 3, "0027", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Jews Targeted", 3, "0028", EventCardEffect.JEWS_TARGETED));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "London Bombed", 3, "0029", EventCardEffect.LONDON_BOMBED));
        stageDecks.get(3).add(new EventCard(EventCardType.NORMAL, "Germany Bombed", 3, "0030", EventCardEffect.GERMANY_BOMBED));
        stageDecks.get(3).add(new EventCard(EventCardType.KEY_EVENT, "Sea Lion Postponed", 3, "0031", EventCardEffect.SEALION_POSTPONED));
        stageDecks.get(3).add(new EventCard(EventCardType.IGNORABLE, "Western Offensive", 3, "0032", EventCardEffect.WESTERN_OFFENSIVE));
        stageDecks.get(3).add(new EventCard(EventCardType.IGNORABLE, "Invasion of Norway", 3, "0033", EventCardEffect.INVASION_OF_NORWAY));
        stageDecks.get(3).add(new EventCard(EventCardType.IGNORABLE, "Fall of Paris", 3, "0034", EventCardEffect.FALL_OF_PARIS));

        // Stage 4
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Visit From Himmler", 4, "0035", EventCardEffect.VISIT_FROM_HIMMLER_4));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "War Conference", 4, "0036", EventCardEffect.WAR_CONFERENCE));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 4, "0037", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Hess Leaves Germany", 4, "0038", EventCardEffect.HESS_LEAVES_GERMANY));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Visit From Goebbels", 4, "0040", EventCardEffect.VISIT_FROM_GOEBBELS_4));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Afrika Korps", 4, "0041", EventCardEffect.AFRIKA_KORPS));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Yugoslavia Surrenders", 4, "0042", EventCardEffect.YUGOSLAVIA_SURRENDERS));
        stageDecks.get(4).add(new EventCard(EventCardType.NORMAL, "Pearl Harbor Attack", 4, "0043", EventCardEffect.PEARL_HARBOR_ATTACK));
        stageDecks.get(4).add(new EventCard(EventCardType.KEY_EVENT, "Fall of Kiev", 4, "0044", EventCardEffect.FALL_OF_KIEV));
        stageDecks.get(4).add(new EventCard(EventCardType.IGNORABLE, "Operation Barbarossa", 4, "0045", EventCardEffect.OPERATION_BARBAROSSA));
        stageDecks.get(4).add(new EventCard(EventCardType.IGNORABLE, "Hitler Assumes Command", 4, "0046", EventCardEffect.HITLER_ASSUMES_COMMAND));

        // Stage 5
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Visit From Bormann", 5, "0039", EventCardEffect.VISIT_FROM_BORMANN_5));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Visit From Goering", 5, "0047", EventCardEffect.VISIT_FROM_GOERING_5));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Casablanca Conference", 5, "0048", EventCardEffect.CASABLANCA_CONFERENCE));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 5, "0049", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Heydrich Assassinated", 5, "0050", EventCardEffect.HEYDRICH_ASSASSINATED));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Berghof Retreat", 5, "0051", EventCardEffect.BERGHOF_RETREAT_5));
        stageDecks.get(5).add(new EventCard(EventCardType.NORMAL, "Rommel Defeated", 5, "0052", EventCardEffect.ROMMEL_DEFEATED));
        stageDecks.get(5).add(new EventCard(EventCardType.KEY_EVENT, "Russian Setback", 5, "0053", EventCardEffect.RUSSIAN_SETBACK));
        stageDecks.get(5).add(new EventCard(EventCardType.IGNORABLE, "Visit to the Front", 5, "0054", EventCardEffect.VISIT_FRONT));
        stageDecks.get(5).add(new EventCard(EventCardType.IGNORABLE, "Eastern Push", 5, "0055", EventCardEffect.EASTERN_PUSH));
        stageDecks.get(5).add(new EventCard(EventCardType.IGNORABLE, "Lebensraum", 5, "0056", EventCardEffect.LEBENSRAUM));

        // Stage 6
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Visit From Hitler", 6, "0057", EventCardEffect.VISIT_FROM_HITLER_6));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Slow Retreat", 6, "0058", EventCardEffect.SLOW_RETREAT));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 6, "0059", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 6, "0060", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Berlin Bombed", 6, "0061", EventCardEffect.BERLIN_BOMBED));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Warsaw Ghetto Uprising", 6, "0062", EventCardEffect.WARSAW_GHETTO_UPRISING));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Stalingrad Surrounded", 6, "0063", EventCardEffect.STALINGRAD_SURROUNDED));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Wolfpacks", 6, "0064", EventCardEffect.WOLFPACKS));
        stageDecks.get(6).add(new EventCard(EventCardType.NORMAL, "Sicily Invaded", 6, "0065", EventCardEffect.SICILY_INVADED));
        stageDecks.get(6).add(new EventCard(EventCardType.KEY_EVENT, "Fortress Europa", 6, "0066", EventCardEffect.FORTRESS_EUROPA));
        stageDecks.get(6).add(new EventCard(EventCardType.IGNORABLE, "Kursk Recaptured", 6, "0067", EventCardEffect.KURSK_RECAPTURED));

        // Stage 7
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "V2 Rocket", 7, "0068", EventCardEffect.V2_ROCKET));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 7, "0069", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Gestapo Raid", 7, "0070", EventCardEffect.GESTAPO_RAID));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Fuhrer Bunker", 7, "0071", EventCardEffect.FUHRER_BUNKER));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Operation Overlord", 7, "0072", EventCardEffect.OPERATION_OVERLORD));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Gothic Line", 7, "0073", EventCardEffect.GOTHIC_LINE));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Rommel Executed", 7, "0074", EventCardEffect.ROMMEL_EXECUTED));
        stageDecks.get(7).add(new EventCard(EventCardType.NORMAL, "Documents Located", 7, "0075", EventCardEffect.DOCUMENTS_LOCATED));
        stageDecks.get(7).add(new EventCard(EventCardType.KEY_EVENT, "Fall of Rome", 7, "0076", EventCardEffect.FALL_OF_ROME));
        stageDecks.get(7).add(new EventCard(EventCardType.IGNORABLE, "Final Conscription", 7, "0077", EventCardEffect.FINAL_CONSCRIPTION));
        stageDecks.get(7).add(new EventCard(EventCardType.IGNORABLE, "Anzio Landing", 7, "0078", EventCardEffect.ANZIO_LANDING));

        // Shuffle all of the decks
        // Discard 2 cards from each deck (remove from game)
        stageDecks.values().stream().forEach(eventCardDeck -> {
            eventCardDeck.shuffle();
            eventCardDeck.draw();
            eventCardDeck.draw();
        });
    }

    public Deck<EventCard> getStageDeck(int stage){
        if (stageDecks.containsKey(stage))
            return stageDecks.get(stage);
        return null;
    }

    public boolean isEmpty(){
        for (Deck<EventCard> stageDeck: stageDecks.values()){
            if (!stageDeck.isEmpty())
                return false;
        }
        return true;
    }
}
