package at.technikum_wien.app.modles;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter

public class Package {
    private List<Card> cards;
    private int price = 5; 
}