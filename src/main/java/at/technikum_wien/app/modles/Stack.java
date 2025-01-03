package at.technikum_wien.app.modles;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    @Getter
    @Setter
    private List<Card> cards;

    public Stack() {
        this.cards = new ArrayList<>();
    }
}
