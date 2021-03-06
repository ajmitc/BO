package bo.game;

import bo.game.conspirator.ConspiratorCard;
import bo.game.conspirator.ConspiratorCardType;
import bo.game.conspirator.ConspiratorDeck;
import bo.game.event.CurrentEventEffect;
import bo.game.event.EventCard;
import bo.game.event.EventCardDeck;
import bo.game.interrogation.InterrogationDeck;
import bo.game.item.Item;
import bo.game.item.ItemDeck;
import bo.game.item.ItemType;
import bo.game.location.Board;
import bo.game.location.LocationName;
import bo.game.player.Player;
import bo.game.plot.Plot;
import bo.game.plot.PlotA;

import java.util.*;

public class Game {
    public static final int MAX_DICE = 10;

    private Phase phase;
    private PhaseStep phaseStep;
    private Difficulty difficulty;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private int militarySupport;
    private ConspiratorDeck conspiratorDeck;
    private EventCardDeck eventCardDeck;
    private InterrogationDeck interrogationDeck;
    private ItemDeck itemDeck;
    private int stage = 1;

    // Dice allocations
    private int dissentTrackDice = 0;
    private int defectionsAndDissentDice = 0;

    private EventCard currentEventCard;
    private EventCard currentKeyEventCard;
    private CurrentEventEffect currentEventEffect;

    private Map<String, Plot> plots = new HashMap<>();

    // Bonus actions awarded to players (ie. Black Orchestra card)
    private Map<Player, Integer> bonusActions = new HashMap<>();


    private int currentPlayerActionsAllowed = 3;
    private int currentPlayerActionsTaken = 0;
    private boolean currentPlayerConspireActionTaken = false;
    private boolean conspireActionDisabled = false;
    private boolean specialAbilityActionDisabled = false;
    private boolean abwehrUseSpecialAbilities = true;
    private boolean hessTokenOnBoard = true;
    private int numEventCardsToResolve = 1;

    public Game(){
        phase = Phase.SETUP;
        phaseStep = PhaseStep.START_PHASE;

        difficulty = Difficulty.STANDARD;
        board = new Board();
        militarySupport = difficulty.getStartingMilitarySupport();
        conspiratorDeck = new ConspiratorDeck();
        eventCardDeck = new EventCardDeck();
        interrogationDeck = new InterrogationDeck();
        itemDeck = new ItemDeck();

        // Create items and distribute to locations
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.BADGE));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.EXPLOSIVES));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.INTEL));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.KEYS));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.MAP));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.POISON));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.SIGNATURE));
        for (int i = 0; i < 3; ++i)
            items.add(new Item(ItemType.WEAPONS));
        Collections.shuffle(items);
        board.getLocations().values().stream()
                .filter(location -> !Board.LOCATIONS_NO_ITEM.contains(location.getName()))
                .forEach(location -> location.setItem(items.remove(0)));

        // Put Nazi leaders at starting locations
        board.getLocation(LocationName.DEUTSCHLANDHALLE).getNaziMembers().add(NaziMember.HITLER);
        board.getLocation(LocationName.MUNICH).getNaziMembers().add(NaziMember.GOERING);
        board.getLocation(LocationName.HANNOVER).getNaziMembers().add(NaziMember.HESS);
        board.getLocation(LocationName.NUREMBERG).getNaziMembers().add(NaziMember.BORMANN);
        board.getLocation(LocationName.MINISTRY_OF_PROPOGANDA).getNaziMembers().add(NaziMember.GOEBBELS);
        board.getLocation(LocationName.GESTAPO_HQ).getNaziMembers().add(NaziMember.HIMMLER);

        PlotA plotA = new PlotA();
        plots.put(plotA.getId(), plotA);
    }

    /**
     * Arrest the given player
     *
     * You can???t take actions or resolve lightning effects.
     * - You can???t be the target of, and are unaffected by, all effects (including effects from event and interrogation cards) unless the effect specifically
     *   states that you are released.
     * - Your special ability is treated as if it were blank.
     * - Once you???ve chosen which option to resolve, read it aloud and apply its effects, but do not reveal the other options.
     *   You choose the target for all effects.
     * - You keep all of your items and non-illegal cards in your dossier.
     *
     * @param player
     */
    public void arrest(Player player){
        player.setArrested(true);
        board.move(player, LocationName.PRISON);
        // Remove all restricted cards from dossier and discard
        player.getDossier().stream().filter(card -> card.getType() == ConspiratorCardType.RESTRICTED).forEach(card -> conspiratorDeck.discard(card));
        player.getDossier().removeIf(card -> card.getType() == ConspiratorCardType.RESTRICTED);
        // TODO For each RESTRICTED card discarded, discard a non-RESTRICTED card
    }

    public List<NaziMember> getDeputiesInPlay(){
        List<NaziMember> deputies = new ArrayList<>();
        board.getLocations().values().stream().forEach(location -> {
            for (NaziMember naziMember: location.getNaziMembers()){
                if (naziMember != NaziMember.HITLER && !deputies.contains(naziMember)){
                    deputies.add(naziMember);
                }
            }
        });
        return deputies;
    }

    /**
     * Return the number of dice available in the pool
     *
     * @return MAX_DICE - dissent-track-dice - defections&dissent-dice
     */
    public int getNumDiceAvailable(){
        return MAX_DICE - dissentTrackDice - defectionsAndDissentDice;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
        this.phaseStep = PhaseStep.START_PHASE;
    }

    public PhaseStep getPhaseStep() {
        return phaseStep;
    }

    public void setPhaseStep(PhaseStep phaseStep) {
        this.phaseStep = phaseStep;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.militarySupport = difficulty.getStartingMilitarySupport();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setNextPlayer(){
        if (currentPlayer != null){
            int index = players.indexOf(currentPlayer);
            index = (index + 1) % players.size();
            currentPlayer = players.get(index);
        }
        else if (!players.isEmpty())
            currentPlayer = players.get(0);
    }

    public int getMilitarySupport() {
        return militarySupport;
    }

    public void setMilitarySupport(int militarySupport) {
        this.militarySupport = militarySupport;
    }

    public void resetMilitarySupport(){
        this.militarySupport = difficulty.getStartingMilitarySupport();
    }

    public void adjMilitarySupport(int amount){
        this.militarySupport += amount;
        if (this.militarySupport < difficulty.getStartingMilitarySupport())
            this.militarySupport = difficulty.getStartingMilitarySupport();
        if (this.militarySupport > 7)
            this.militarySupport = 7;
    }

    public ConspiratorDeck getConspiratorDeck() {
        return conspiratorDeck;
    }

    public EventCardDeck getEventCardDeck() {
        return eventCardDeck;
    }

    public InterrogationDeck getInterrogationDeck() {
        return interrogationDeck;
    }

    public ItemDeck getItemDeck() {
        return itemDeck;
    }

    public Board getBoard() {
        return board;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public EventCard getCurrentEventCard() {
        return currentEventCard;
    }

    public void setCurrentEventCard(EventCard currentEventCard) {
        this.currentEventCard = currentEventCard;
    }

    public EventCard getCurrentKeyEventCard() {
        return currentKeyEventCard;
    }

    public void setCurrentKeyEventCard(EventCard currentKeyEventCard) {
        this.currentKeyEventCard = currentKeyEventCard;
    }

    public int getDissentTrackDice() {
        return dissentTrackDice;
    }

    public void setDissentTrackDice(int dissentTrackDice) {
        this.dissentTrackDice = dissentTrackDice;
    }

    public void adjDissentTrackDice(int amount) {
        this.dissentTrackDice += amount;
    }

    public int getDefectionsAndDissentDice() {
        return defectionsAndDissentDice;
    }

    public void setDefectionsAndDissentDice(int defectionsAndDissentDice) {
        this.defectionsAndDissentDice = defectionsAndDissentDice;
    }

    public void adjDefectionsAndDissentDice(int amount){
        this.defectionsAndDissentDice += amount;
    }

    public boolean isHessTokenOnBoard() {
        return hessTokenOnBoard;
    }

    public void setHessTokenOnBoard(boolean hessTokenOnBoard) {
        this.hessTokenOnBoard = hessTokenOnBoard;
    }

    public int getNumEventCardsToResolve() {
        return numEventCardsToResolve;
    }

    public void setNumEventCardsToResolve(int numEventCardsToResolve) {
        this.numEventCardsToResolve = numEventCardsToResolve;
    }

    public Map<String, Plot> getPlots() {
        return plots;
    }

    public boolean isAbwehrUseSpecialAbilities() {
        return abwehrUseSpecialAbilities;
    }

    public void setAbwehrUseSpecialAbilities(boolean abwehrUseSpecialAbilities) {
        this.abwehrUseSpecialAbilities = abwehrUseSpecialAbilities;
    }

    public CurrentEventEffect getCurrentEventEffect() {
        return currentEventEffect;
    }

    public void setCurrentEventEffect(CurrentEventEffect currentEventEffect) {
        this.currentEventEffect = currentEventEffect;
    }

    public int getCurrentPlayerActionsAllowed() {
        return currentPlayerActionsAllowed;
    }

    public void setCurrentPlayerActionsAllowed(int currentPlayerActionsAllowed) {
        this.currentPlayerActionsAllowed = currentPlayerActionsAllowed;
    }

    public void adjCurrentPlayerActionsAllowed(int amount){
        this.currentPlayerActionsAllowed += amount;
    }

    public int getCurrentPlayerActionsTaken() {
        return currentPlayerActionsTaken;
    }

    public void setCurrentPlayerActionsTaken(int currentPlayerActionsTaken) {
        this.currentPlayerActionsTaken = currentPlayerActionsTaken;
    }

    public void adjCurrentPlayerActionsTaken(int amount) {
        this.currentPlayerActionsTaken += amount;
    }

    public boolean isCurrentPlayerConspireActionTaken() {
        return currentPlayerConspireActionTaken;
    }

    public void setCurrentPlayerConspireActionTaken(boolean currentPlayerConspireActionTaken) {
        this.currentPlayerConspireActionTaken = currentPlayerConspireActionTaken;
    }

    public boolean isConspireActionDisabled() {
        return conspireActionDisabled;
    }

    public void setConspireActionDisabled(boolean conspireActionDisabled) {
        this.conspireActionDisabled = conspireActionDisabled;
    }

    public boolean isSpecialAbilityActionDisabled() {
        return specialAbilityActionDisabled;
    }

    public void setSpecialAbilityActionDisabled(boolean specialAbilityActionDisabled) {
        this.specialAbilityActionDisabled = specialAbilityActionDisabled;
    }

    public Map<Player, Integer> getBonusActions() {
        return bonusActions;
    }
}
