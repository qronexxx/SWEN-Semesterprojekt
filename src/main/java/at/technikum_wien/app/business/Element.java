package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.Card;

public abstract class Element {
    public abstract double getEffectivnessAgainst(Card opponendCard);
}
