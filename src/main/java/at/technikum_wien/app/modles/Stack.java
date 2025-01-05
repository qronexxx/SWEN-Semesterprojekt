package at.technikum_wien.app.modles;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@Setter
public class Stack {
    @JsonProperty("Cards")
    private List<Card> cards;

    public Stack() {
        this.cards = new ArrayList<>();
    }
}