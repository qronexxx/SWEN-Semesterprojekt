package at.technikum_wien.app.modles;
import at.technikum_wien.app.business.CardCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User {
    private String username;
    private String password;
    private int coins;
    private int elo;
    private CardCollection stack;
    private CardCollection deck;

    public User(String username, String password, int coins, int elo){
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.elo = elo;
        this.deck = new CardCollection(4);
        this.stack = new CardCollection();
    }
}
