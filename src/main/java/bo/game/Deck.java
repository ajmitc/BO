package bo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T> {
    private List<T> cards = new ArrayList<>();
    private List<T> discardPile = new ArrayList<>();

    public Deck(){

    }

    public T draw(){
        return cards.remove(0);
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
}
