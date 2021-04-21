package bo.game.player;

import bo.game.conspirator.ConspiratorCard;
import bo.game.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    public static final String BECK         = "Beck";
    public static final String BONHOEFFER   = "Bonhoeffer";
    public static final String CANARIS      = "Canaris";
    public static final String GOERDELER    = "Goerdeler";
    public static final String KORDT        = "Kordt";
    public static final String OLBRICHT     = "Olbricht";
    public static final String OSTER        = "Oster";
    public static final String STAUFFENBERG = "Stauffenberg";
    public static final String TRESCKOW     = "Tresckow";

    private PlayerType type;
    private String name; // Character name (ie. Beck, Oster, etc)
    private List<Item> items = new ArrayList<>();
    private Motivation motivation;
    private Suspicion suspicion;
    private List<ConspiratorCard> dossier = new ArrayList<>();
    private Set<PlayerSpecialAbility> specialAbilities = new HashSet<>();

    private boolean arrested = false;

    public Player(String name){
        this.name = name;
        this.motivation = Motivation.TIMID;
        this.suspicion = Suspicion.MEDIUM;

        if (name.equals(BONHOEFFER) || name.equals(CANARIS) || name.equals(OSTER))
            type = PlayerType.ABWEHR;
        else if (name.equals(GOERDELER) || name.equals(KORDT))
            type = PlayerType.CIVILIAN;
        else if (name.equals(BECK) || name.equals(OLBRICHT) || name.equals(STAUFFENBERG) || name.equals(TRESCKOW))
            type = PlayerType.WEHRMACHT;


        if (name.equals(BONHOEFFER)){
            specialAbilities.add(PlayerSpecialAbility.ACTION_INCREASE_MOTIVATION_OF_OTHER_CONSPIRATORS_SAME_LOCATION);
        }
        else if (name.equals(CANARIS)){
            specialAbilities.add(PlayerSpecialAbility.ACTION_DISCARD_ITEM_TO_LOWER_SUSPICION_OF_ANY_PLAYER);
        }
        else if (name.equals(OSTER)) {
            specialAbilities.add(PlayerSpecialAbility.ACTION_DRAW_2_CARDS_KEEP_ONE_DISCARD_OTHER);
        }
        else if (name.equals(GOERDELER)){
            specialAbilities.add(PlayerSpecialAbility.DRAW_1_CONSPIRATOR_CARD_AT_START_OF_TURN);
            specialAbilities.add(PlayerSpecialAbility.HAND_LIMIT_PLUS_2);
        }
        else if (name.equals(KORDT)) {
            specialAbilities.add(PlayerSpecialAbility.ACTION_REARRANGE_TOP_THREE_EVENT_CARDS);
        }
        else if (name.equals(BECK)){
            specialAbilities.add(PlayerSpecialAbility.ADD_DIE_TO_PLOT_IN_SAME_LOCATION);
            specialAbilities.add(PlayerSpecialAbility.HITLER_AND_DEPUTY_TOKENS_NO_EFFECT);
        }
        else if (name.equals(OLBRICHT)){
            specialAbilities.add(PlayerSpecialAbility.ACTION_MOVE_2_SPACES_OR_MOVE_ANOTHER_CONSPIRATOR_1_SPACE);
        }
        else if (name.equals(STAUFFENBERG)){
            specialAbilities.add(PlayerSpecialAbility.TAKE_4_ACTIONS);
        }
        else if (name.equals(TRESCKOW)) {
            specialAbilities.add(PlayerSpecialAbility.HOLD_PLUS_1_ITEM);
            specialAbilities.add(PlayerSpecialAbility.ACTION_PICK_UP_UNREVEALED_ITEM_SAME_LOCATION);
            specialAbilities.add(PlayerSpecialAbility.ACTION_REVEAL_ANY_ITEM_TILE);
        }
    }

    public int getDossierMaxSize(int numPlayers){
        int maxCards = 4;
        if (motivation == Motivation.TIMID)
            maxCards = 2;
        else if (numPlayers <= 2)
            maxCards = 6;
        else if (numPlayers <= 4)
            maxCards = 5;
        if (hasSpecialAbility(PlayerSpecialAbility.HAND_LIMIT_PLUS_2))
            maxCards += 2;
        return maxCards;
    }

    public int getMaxItems(int numPlayers){
        int maxItems = numPlayers <= 2? 4: 3;
        if (hasSpecialAbility(PlayerSpecialAbility.HOLD_PLUS_1_ITEM))
            maxItems += 1;
        return maxItems;
    }

    public String getName() {
        return name;
    }

    public PlayerType getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public Motivation getMotivation() {
        return motivation;
    }

    public void setMotivation(Motivation motivation) {
        this.motivation = motivation;
    }

    public Suspicion getSuspicion() {
        return suspicion;
    }

    public void setSuspicion(Suspicion suspicion) {
        this.suspicion = suspicion;
    }

    public List<ConspiratorCard> getDossier() {
        return dossier;
    }

    public boolean isArrested() {
        return arrested;
    }

    public void setArrested(boolean arrested) {
        this.arrested = arrested;
    }

    public Set<PlayerSpecialAbility> getSpecialAbilities() {
        return specialAbilities;
    }

    public boolean hasSpecialAbility(PlayerSpecialAbility specialAbility){
        return specialAbilities.contains(specialAbility);
    }
}
