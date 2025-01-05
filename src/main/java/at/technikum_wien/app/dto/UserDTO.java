package at.technikum_wien.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Bio")
    private String bio;

    @JsonProperty("Image")
    private String image;

    public UserDTO() {}

    public UserDTO(String name, String bio, String image) {
        this.name = name;
        this.bio = bio;
        this.image = image;
    }
}
