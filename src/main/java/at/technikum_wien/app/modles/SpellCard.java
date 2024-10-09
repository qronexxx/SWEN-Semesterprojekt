package at.technikum_wien.app.modles;

import at.technikum_wien.app.business.Element;

public class SpellCard extends Card{
    private String SpellType;

    public SpellCard(String name, int damage, Element element){
        super(name, damage, element);
    }

}
