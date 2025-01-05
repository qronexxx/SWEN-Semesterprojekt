package at.technikum_wien.app.modles;

import at.technikum_wien.app.dto.CardDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Deck {
    private static final int MAX_CARDS = 4;
    private List<Card> cards = new ArrayList<>();

    public boolean addCard(Card card) {
        if (cards.size() >= MAX_CARDS) return false;
        return cards.add(card);
    }

    public boolean isValid() {
        return cards.size() == MAX_CARDS;
    }

    public void deleteCard(int index) {
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
