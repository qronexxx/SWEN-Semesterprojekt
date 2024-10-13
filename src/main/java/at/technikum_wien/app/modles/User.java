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
    private boolean isAdmin;

    public User(){
            this.deck = new CardCollection(4);
            this.stack = new CardCollection();
            setElo(100);
            setCoins(50);
            setAdmin(false);
    }

    public User(String username, String password){
        setUsername(username);
        setPassword(password);
        setCoins(50);
        setElo(100);
        this.deck = new CardCollection(4);
        this.stack = new CardCollection();
        setAdmin(false);
    }

    public User(String username, String password, boolean isAdmin){
        setUsername(username);
        setPassword(password);
        setCoins(50);
        setElo(100);
        setAdmin(isAdmin);
        this.deck = new CardCollection(4);
        this.stack = new CardCollection();
    }
}
