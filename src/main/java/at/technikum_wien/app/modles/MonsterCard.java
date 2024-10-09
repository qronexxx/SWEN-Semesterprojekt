package at.technikum_wien.app.modles;

import at.technikum_wien.app.business.Element;

public class MonsterCard extends Card{
    private String MonsterType;

    public MonsterCard(String name, int damage, Element element){
        super(name, damage, element);
    }
}
