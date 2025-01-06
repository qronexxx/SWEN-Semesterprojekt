package at.technikum_wien.app.modles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TradingDeal {
    @JsonProperty("Id")
    private String id;

    @JsonIgnore
    private String username;

    @JsonProperty("CardToTrade")
    private String cardToTrade;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("MinimumDamage")
    private double minimumDamage;

    public TradingDeal() {}
}