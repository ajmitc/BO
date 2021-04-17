package bo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T> {
    private List<T> cards = new ArrayList<>();
    private List<T> discardPile = new ArrayList<>();
    private boolean shuffleDiscardIfDeckEmpty = true;

    public Deck(){

    }

    public T draw(){
        if (cards.isEmpty() && shuffleDiscardIfDeckEmpty){
            shuffleDiscardIntoDeck();
        }
        if (!cards.isEmpty())
            return cards.remove(0);
        return null;
    }

    public void add(T card){
        cards.add(card);
    }

    public void discard(T card){
        discardPile.add(card);
    }

    public void shuffleDiscardIntoDeck(){
        cards.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public int size(){
        return cards.size();
    }

    public void setShuffleDiscardIfDeckEmpty(boolean shuffleDiscardIfDeckEmpty) {
        this.shuffleDiscardIfDeckEmpty = shuffleDiscardIfDeckEmpty;
    }

    public boolean isShuffleDiscardIfDeckEmpty() {
        return shuffleDiscardIfDeckEmpty;
    }
}
