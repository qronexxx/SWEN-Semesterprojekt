package at.technikum_wien.app.modles;

import lombok.Getter;

public class WaterElement extends Element {
    public String getElementType() {
        return "Water";
    }
    @Override
    public double getEffectivnessAgainst(SpellCard opponendCard) {
        if(opponendCard.getElement().equals("Fire")){
            return 2;
        } else if (opponendCard.getElement().equals("Normal")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}