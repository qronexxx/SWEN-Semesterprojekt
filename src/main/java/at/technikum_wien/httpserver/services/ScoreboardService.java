package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.ScoreboardController;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class ScoreboardService implements Service {
    private final ScoreboardController scoreboardController;

    public ScoreboardService() {
        this.scoreboardController = new ScoreboardController(new UnitOfWork());
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

        if (request.getMethod() == Method.GET) {
            return this.scoreboardController.showScoreboard();
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
