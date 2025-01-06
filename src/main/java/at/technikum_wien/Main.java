package at.technikum_wien;
import at.technikum_wien.app.business.*;
import at.technikum_wien.app.modles.*;
import at.technikum_wien.httpserver.server.Server;
import at.technikum_wien.httpserver.services.*;
import at.technikum_wien.httpserver.utils.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        //test();

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
        router.addService("/transactions", new TransactionsService());
        router.addService("/cards", new CardsService());
        router.addService("/deck", new DeckService());
        router.addService("/stats", new StatsService());
        router.addService("/scoreboard", new ScoreboardService());
        router.addService("/battles", new BattlesService());
        router.addService("/tradings", new TradingsService());

        return router;
    }

    static void test(){
        User player1 = new User("Player1", "password");
        User player2 = new User("Player2", "password");

        // Add some cards to player1's deck
        player1.getDeck().addCard(new Card("Dragon", 50));
        player1.getDeck().addCard(new Card("FireSpell", 30));
        player1.getDeck().addCard(new Card("Wizard", 40));
        player1.getDeck().addCard(new Card("Ork", 40));

        // Add some cards to player2's deck
        player2.getDeck().addCard(new Card("Goblin", 45));
        player2.getDeck().addCard(new Card("Knight", 55));
        player2.getDeck().addCard(new Card("Kraken", 55));
        player2.getDeck().addCard(new Card("FireElf", 55));

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
