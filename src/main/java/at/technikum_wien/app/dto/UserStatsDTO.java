package at.technikum_wien.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatsDTO {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Elo")
    private int elo;

    @JsonProperty("Wins")
    private int wins;

    @JsonProperty("Losses")
    private int losses;

    public UserStatsDTO() {
    }

    public UserStatsDTO(String name, int elo, int wins, int losses) {
        setName(name);
        setElo(elo);
        setWins(wins);
        setLosses(losses);
    }
}