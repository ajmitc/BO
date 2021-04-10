package bo.game.conspirator;

import bo.game.Deck;

public class ConspiratorDeck extends Deck<ConspiratorCard> {

    public ConspiratorDeck(){
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Defections and Dissent", "0000", ConspiratorCardEffect.DEFECTIONS_AND_DISSENT, ConspiratorCardPlayTiming.ONGOING));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "False Accusations", "0001", ConspiratorCardEffect.FALSE_ACCUSATIONS, ConspiratorCardPlayTiming.ACTION_AFTER_FAILING_RESIST_INTERROGATION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Black Orchestra", "0002", ConspiratorCardEffect.BLACK_ORCHESTRA, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Planning Session", "0003", ConspiratorCardEffect.PLANNING_SESSION, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Planning Session", "0004", ConspiratorCardEffect.PLANNING_SESSION, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Quick Reaction", "0005", ConspiratorCardEffect.QUICK_REACTION, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Quick Reaction", "0006", ConspiratorCardEffect.QUICK_REACTION, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Effective Planning", "0007", ConspiratorCardEffect.EFFECTIVE_PLANNING, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Dumpster Dive", "0008", ConspiratorCardEffect.DUMPSTER_DIVE, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Dumpster Dive", "0009", ConspiratorCardEffect.DUMPSTER_DIVE, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Delay Hitler", "0010", ConspiratorCardEffect.DELAY_HITLER, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Delay Hitler", "0011", ConspiratorCardEffect.DELAY_HITLER, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Slow News Day", "0012", ConspiratorCardEffect.SLOW_NEWS_DAY, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Slow News Day", "0013", ConspiratorCardEffect.SLOW_NEWS_DAY, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Safe", "0014", ConspiratorCardEffect.SAFE, ConspiratorCardPlayTiming.ONGOING));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Airplane Access", "0015", ConspiratorCardEffect.AIRPLANE_ACCESS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Airplane Access", "0016", ConspiratorCardEffect.AIRPLANE_ACCESS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Hitler's Schedule Leaked", "0017", ConspiratorCardEffect.HITLERS_SCHEDULE_LEAKED, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Infiltrate Bunker", "0018", ConspiratorCardEffect.HITLERS_SCHEDULE_LEAKED, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Black Market", "0019", ConspiratorCardEffect.BLACK_MARKET, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Black Market", "0020", ConspiratorCardEffect.BLACK_MARKET, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Cover Story", "0021", ConspiratorCardEffect.COVER_STORY, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "History Repeats", "0022", ConspiratorCardEffect.HISTORY_REPEATS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Called Away", "0023", ConspiratorCardEffect.CALLED_AWAY, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Distant Contact", "0024", ConspiratorCardEffect.DISTANT_CONTACT, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Lose Gestapo", "0025", ConspiratorCardEffect.LOSE_GESTAPO, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.NORMAL, "Lose Gestapo", "0026", ConspiratorCardEffect.LOSE_GESTAPO, ConspiratorCardPlayTiming.ACTION));

        // Plots
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Lone Revolver", "0000_A", ConspiratorCardEffect.LONE_REVOLVER, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Suitcase Bomb", "0001_B", ConspiratorCardEffect.SUITCASE_BOMB, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Derail Train", "0002_C", ConspiratorCardEffect.DERAIL_TRAIN, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Plane Bomb", "0003_D", ConspiratorCardEffect.PLANE_BOMB, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Hidden Bomb", "0004_E", ConspiratorCardEffect.HIDDEN_BOMB, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Coup", "0005_F", ConspiratorCardEffect.COUP, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Poison Gas", "0006_G", ConspiratorCardEffect.POISON_GAS, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Sniper", "0007_H", ConspiratorCardEffect.SNIPER, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Kidnapping", "0008_J", ConspiratorCardEffect.KIDNAPPING, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Poison Parcel", "0009_K", ConspiratorCardEffect.POISON_PARCEL, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Poison Food", "0010_L", ConspiratorCardEffect.POISON_FOOD, null));
        add(new ConspiratorCard(ConspiratorCardType.PLOT, "Ambush", "0011_M", ConspiratorCardEffect.AMBUSH, null));

        // Restricted
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Partisan Informant", "0000", ConspiratorCardEffect.PARTISAN_INFORMANT, ConspiratorCardPlayTiming.ANY_TURN_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Classified Papers", "0001", ConspiratorCardEffect.CLASSIFIED_PAPERS, ConspiratorCardPlayTiming.ONGOING));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "War Crimes Evidence", "0002", ConspiratorCardEffect.WAR_CRIMES_EVIDENCE, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "War Crimes Evidence", "0003", ConspiratorCardEffect.WAR_CRIMES_EVIDENCE, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Forged Documents", "0004", ConspiratorCardEffect.FORGED_DOCUMENTS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Forged Documents", "0005", ConspiratorCardEffect.FORGED_DOCUMENTS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Military Secrets", "0006", ConspiratorCardEffect.MILITARY_SECRETS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Military Secrets", "0007", ConspiratorCardEffect.MILITARY_SECRETS, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Intelligence Briefing", "0008", ConspiratorCardEffect.INTELLIGENCE_BRIEFING, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Hidden Camera", "0009", ConspiratorCardEffect.HIDDEN_CAMERA, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "False Safety Report", "0010", ConspiratorCardEffect.FALSE_SAFETY_REPORT, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Inspiring Letter", "0011", ConspiratorCardEffect.INSPIRING_LETTER, ConspiratorCardPlayTiming.ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Officer Recruited", "0012", ConspiratorCardEffect.OFFICER_RECRUITED, ConspiratorCardPlayTiming.INITIATING_PLOT_NOT_AN_ACTION));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Concealed Pistol", "0013", ConspiratorCardEffect.CONCEALED_PISTOL, ConspiratorCardPlayTiming.ONGOING));
        add(new ConspiratorCard(ConspiratorCardType.RESTRICTED, "Forged Release Orders", "0014", ConspiratorCardEffect.FORGED_RELEASE_ORDERS, ConspiratorCardPlayTiming.ACTION));

        shuffle();
    }
}
