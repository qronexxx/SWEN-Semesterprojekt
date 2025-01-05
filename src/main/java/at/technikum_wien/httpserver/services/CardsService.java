package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.CardsController;
import at.technikum_wien.app.controllers.TransactionsController;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class CardsService implements Service {
    private final CardsController cardsController;
    private final UnitOfWork unitOfWork;

    public CardsService() {
        this.unitOfWork = new UnitOfWork();
        this.cardsController = new CardsController(unitOfWork);
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
            return this.cardsController.getUserCards(username);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
