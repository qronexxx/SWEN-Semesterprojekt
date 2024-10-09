package at.technikum_wien.app.modles;

import at.technikum_wien.app.business.Element;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Card {
    protected String name;
    protected int damage;
    protected Element element;

    public Card(String name, int damage, Element element){
        setName(name);
        setDamage(damage);
        setElement(element);
    }
}

