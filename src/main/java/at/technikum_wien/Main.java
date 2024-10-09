package at.technikum_wien;
import at.technikum_wien.app.business.BattleField;
import at.technikum_wien.app.business.FireElement;
import at.technikum_wien.app.business.NormalElement;
import at.technikum_wien.app.business.WaterElement;
import at.technikum_wien.app.modles.*;

public class Main {
    public static void main(String[] args) {
        User player1 = new User("Player1", "1000",  50,  100);
        User player2 = new User("Player2", "1000",  50,  100);

        // Add some cards to player1's deck
        //All different monster cards: Goblins Dragons Wizzard Orks Knights Kraken FireElves
        player1.getDeck().addCard(new MonsterCard("Dragon", 50, new FireElement()));
        player1.getDeck().addCard(new SpellCard("Fireball", 30, new FireElement()));
        player1.getDeck().addCard(new MonsterCard("Wizard", 40, new FireElement()));
        player1.getDeck().addCard(new MonsterCard("Ork", 40, new NormalElement()));


        // Add some cards to player2's deck
        player2.getDeck().addCard(new MonsterCard("Goblins", 45, new WaterElement()));
        player2.getDeck().addCard(new MonsterCard("Knights", 55, new NormalElement()));
        player2.getDeck().addCard(new MonsterCard("Kraken", 55, new WaterElement()));
        player2.getDeck().addCard(new MonsterCard("FireElves", 55, new FireElement()));

        BattleField battleField = new BattleField(player1, player2);
        String result = battleField.startBattle();

        System.out.println("Battle Result: " + result);
        System.out.println("Battle Log:");
        for (String log : battleField.getBattleLog()) {
            System.out.println(log);
        }

        System.out.println("Player1 ELO: " + player1.getElo());
        System.out.println("Player2 ELO: " + player2.getElo());
    }
}