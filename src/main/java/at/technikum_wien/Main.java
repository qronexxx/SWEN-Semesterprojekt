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
        router.addService("/transactions", new TransactionsService());
        router.addService("/cards", new CardsService());
        router.addService("/deck", new DeckService());
        router.addService("/stats", new StatsService());
        router.addService("/scoreboard", new ScoreboardService());
        router.addService("/battles", new BattlesService());
        router.addService("/tradings", new TradingsService());

        return router;
    }
}
