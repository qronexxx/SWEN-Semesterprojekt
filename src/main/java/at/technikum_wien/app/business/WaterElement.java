package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.Card;

public class WaterElement extends Element {
    public String getElementType() {
        return "Water";
    }
    @Override
    public double getEffectivenessAgainst(Card opponendCard) {
        if(opponendCard.getElement().equals("Fire")){
            return 2;
        } else if (opponendCard.getElement().equals("Normal")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}