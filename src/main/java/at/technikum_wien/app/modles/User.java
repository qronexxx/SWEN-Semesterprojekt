package at.technikum_wien.app.modles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    @JsonProperty("Username")
    private String Username;

    @JsonProperty("Password")
    private String Password;

    @JsonProperty("Name")
    private String Name;
    private int Coins;
    private int Elo;

    @JsonProperty("Bio")
    private String Bio;

    @JsonProperty("Image")
    private String Image;
    private Stack Stack;
    private Deck deck;
    private int losses;
    private int wins;

    public User(){
        this.deck = new Deck();
        setElo(100);
        setCoins(20);
        setName("");
        setBio("");
        setImage("");
    }

    public User(String username, String bio, String image){
        setUsername(username);
        setBio(bio);
        setImage(image);
    }

    public User(String username, String password){
        setUsername(username);
        setPassword(password);
        setCoins(20);
        setElo(100);
        this.deck = new Deck();
        setName("");
        setBio("");
        setImage("");
    }

    public User(String username, String password, String name, int coins, int elo, int wins, int losses, String bio, String image){
        setUsername(username);
        setPassword(password);
        setName(name);
        setCoins(coins);
        setElo(elo);
        setWins(wins);
        setBio(bio);
        setImage(image);
        setLosses(losses);
        setStack(new Stack());
        setDeck(deck);
    }
}
