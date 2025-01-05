package at.technikum_wien.app.modles;

public enum Element {
    WATER,
    FIRE,
    REGULAR;

    public static Element fromName(String cardName) {
        if (cardName.startsWith("Water")) {
            return WATER;
        } else if (cardName.startsWith("Fire")) {
            return FIRE;
        } else {
            return REGULAR;
        }
    }
}
