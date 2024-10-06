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
        if(cards.size() < maxSize){
            cards.add(card);
        }else{
            System.out.println("Die maximale Anzahl ist bereits erreicht");
        }
    }

    public void deleteCard(int index){
        cards.remove(index);
    }
}
