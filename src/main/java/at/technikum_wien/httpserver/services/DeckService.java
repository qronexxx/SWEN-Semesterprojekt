package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.controllers.DeckController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class DeckService implements Service {
    private final DeckController deckController;

    public DeckService() {
        this.deckController = new DeckController();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return this.deckController.showDeck();
        } else if (request.getMethod() == Method.PUT){
            return this.deckController.configureDeck();
        }


        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
