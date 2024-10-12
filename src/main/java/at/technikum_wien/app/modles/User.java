package at.technikum_wien.app.modles;
import at.technikum_wien.app.business.CardCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User {
    private int userID;
    private String username;
    private String password;
    private int coins;
    private int elo;
    private CardCollection stack;
    private CardCollection deck;

    public User(int userID, String username, String password, int coins, int elo){
        setUserID(userID);
        setUsername(username);
        setPassword(password);
        setCoins(coins);
        setElo(elo);
        this.deck = new CardCollection(4);
        this.stack = new CardCollection();
    }
}
