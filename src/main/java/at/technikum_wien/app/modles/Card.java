package at.technikum_wien.app.modles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter


public class Card {
    @JsonProperty("Id")
    protected String Id;
    @JsonProperty("Name")
    protected String name;
    @JsonProperty("Damage")
    protected int damage;

    public Card() {}

    public Card(UUID id, String name, int damage){
        setId(UUID.randomUUID().toString());
        setName(name);
        setDamage(damage);
    }

    public Card(String name, int damage){
        setName(name);
        setDamage(damage);
    }

    @JsonIgnore
    public Element getElement() {
        if (name.startsWith("Water")) {
            return Element.WATER;
        } else if (name.startsWith("Fire")) {
            return Element.FIRE;
        } else {
            return Element.REGULAR;
        }
    }

    @JsonIgnore
    public String getType() {
        return getElement().toString().toLowerCase();
    }
}

