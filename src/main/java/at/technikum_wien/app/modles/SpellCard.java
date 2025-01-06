package at.technikum_wien.app.modles;

public class SpellCard extends Card{
    private String SpellType;

    public SpellCard(String name, int damage){
        super(name, damage);
    }

    public SpellCard(String name, int damage, Element element){
        super(name, damage);
    }


}

