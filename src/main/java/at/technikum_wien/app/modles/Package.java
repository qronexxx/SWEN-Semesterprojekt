package at.technikum_wien.app.modles;
import java.util.ArrayList;
import java.util.List;

import at.technikum_wien.app.dto.CardDTO;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter

public class Package {
    private ArrayList<CardDTO> cards;
}