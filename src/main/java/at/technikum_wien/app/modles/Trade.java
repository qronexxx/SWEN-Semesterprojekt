package at.technikum_wien.app.modles;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Trade {
    private String id;
    private String cardToTrade; // ID from the card to trade
    private String type;
    private int minimumDamage;
}
