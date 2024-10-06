package at.technikum_wien.app.modles;

import lombok.Getter;

public class FireElement extends Element {
    public String getElementType() {
        return "Fire";
    }
    @Override
    public double getEffectivnessAgainst(SpellCard opponendCard) {
        if(opponendCard.getElement().equals("Normal")){
            return 2;
        } else if (opponendCard.getElement().equals("Water")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}
