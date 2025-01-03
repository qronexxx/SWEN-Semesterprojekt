package at.technikum_wien.app.modles;
import at.technikum_wien.app.business.CardCollection;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAlias;
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
    private String Name;
    private int Coins;
    private int Elo;
    private String Bio;
    private String Image;
    private Stack Stack;
    private Deck Deck;
    private int Losses;
    private int Wins;

    public User(){
            this.Deck = new Deck();
            this.Stack = new Stack();
            setElo(100);
            setCoins(20);
            setName("");
            setBio("");
            setImage("");
    }

    public User(String Username, String Password){
        setUsername(Username);
        setPassword(Password);
        setCoins(20);
        setElo(100);
        this.Deck = new Deck();
        this.Stack = new Stack();
        setName("");
        setBio("");
        setImage("");
    }

    public User(String Username, String Password, boolean isAdmin){
        setUsername(Username);
        setPassword(Password);
        setCoins(20);
        setElo(100);
        this.Deck = new Deck();
        this.Stack = new Stack();
        setName("");
        setBio("");
        setImage("");
    }
}
