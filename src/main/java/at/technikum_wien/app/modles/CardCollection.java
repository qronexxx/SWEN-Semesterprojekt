package at.technikum_wien.app.modles;

import java.util.ArrayList;
import java.util.List;

public class CardCollection {
    private List<Card> cards;
    private int maxSize;

    public CardCollection(){
        this.cards = new ArrayList<>();
    }

    public CardCollection(int maxSize){
        this.cards = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public void addCard(Card card){
            cards.add(card);
    }

    public void deleteCard(int index){
        cards.remove(index);
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public Card getCardByIndex(int index) {
        return cards.get(index);
    }
    public int size() {
        return cards.size();
    }
}
