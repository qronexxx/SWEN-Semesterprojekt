package at.technikum_wien.app.modles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Card {
    protected String name;
    protected int damage;
    protected Element element;

    public Card(String name, int damage, Element element){
        this.name = name;
        this.damage = damage;
        this.element = element;
    }
}
