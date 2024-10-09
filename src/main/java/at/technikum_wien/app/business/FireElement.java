package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.Card;

public class FireElement extends Element {
    public String getElementType() {
        return "Fire";
    }
    @Override
    public double getEffectivnessAgainst(Card opponendCard) {
        if(opponendCard.getElement().equals("Normal")){
            return 2;
        } else if (opponendCard.getElement().equals("Water")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}
