package at.technikum_wien;
import at.technikum_wien.app.business.*;
import at.technikum_wien.app.modles.*;
import at.technikum_wien.httpserver.server.Server;
import at.technikum_wien.httpserver.services.*;
import at.technikum_wien.httpserver.utils.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {


        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UserService());
        router.addService("/sessions", new SessionService());
        router.addService("/packages", new PackagesService());
        router.addService("/transactions/packages", new TransactionsService());
        router.addService("/cards", new CardsService());
        router.addService("/deck", new DeckService());
        router.addService("/stats", new StatsService());
        router.addService("/scoreboard", new ScoreboardService());
        router.addService("/battles", new BattlesService());
        router.addService("/tradings", new TradingsService());


        return router;
    }







    void test(){
        User player1 = new User("Player1", "1000");
        User player2 = new User("Player2", "1000");

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
