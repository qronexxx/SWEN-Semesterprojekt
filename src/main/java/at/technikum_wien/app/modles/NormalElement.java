package at.technikum_wien.app.modles;

public class NormalElement extends Element {
    @Override
    public double getEffectivnessAgainst(SpellCard opponendCard) {
        if(opponendCard.getElement().equals("Water")){
            return 2;
        } else if (opponendCard.getElement().equals("Fire")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}
