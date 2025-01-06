package at.technikum_wien.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class CardDTO {
    @JsonProperty("Id")
    private UUID Id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Damage")
    private int damage;

    public CardDTO() {}

    public CardDTO(UUID id, String name,int damage){
        setId(id);
        setName(name);
        setDamage(damage);
    }
}
