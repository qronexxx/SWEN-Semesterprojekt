package at.technikum_wien.app.modles;

public class WaterElement extends Element {
    public String getElementType() {
        return "Water";
    }
    @Override
    public double getEffectivnessAgainst(Card opponendCard) {
        if(opponendCard.getElement().equals("Fire")){
            return 2;
        } else if (opponendCard.getElement().equals("Normal")) {
            return 0.5;
        }else{
            return 1;
        }
    }
}