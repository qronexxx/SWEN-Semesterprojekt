package at.technikum_wien.app.modles;

import at.technikum_wien.app.business.Element;
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

    public Card(String id,String name, int damage){
        setId(UUID.randomUUID().toString());
        setName(name);
        setDamage(damage);
    }
}

