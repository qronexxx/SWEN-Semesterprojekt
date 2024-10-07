package at.technikum_wien.app.modles;

public class NormalElement extends Element {
    public String getElementType() {
        return "Normal";
    }
    @Override
    public double getEffectivnessAgainst(Card opponendCard) {
        if(opponendCard.getElement().equals("Water")){
            return 2;
        } else if (opponendCard.getElement().equals("Fire")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}
