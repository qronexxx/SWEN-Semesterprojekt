package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.DeckController;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class DeckService implements Service {
    private final DeckController deckController;

    public DeckService() {
        this.deckController = new DeckController(new UnitOfWork());
    }

    @Override
    public Response handleRequest(Request request) {
        if (!AuthenticationFilter.isAuthenticated(request)) {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"Authentication required\" }"
            );
        }

        String username = AuthenticationFilter.getUsername(request);

        if (request.getMethod() == Method.GET) {
            return this.deckController.getUserDeck(username);
        } else if (request.getMethod() == Method.PUT){
            return this.deckController.setUserDeck(username, request);
        }


        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
