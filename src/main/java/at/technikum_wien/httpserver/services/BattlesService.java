package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.BattlesController;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class BattlesService implements Service {
    private final BattlesController battlesController;

    public BattlesService() {
        this.battlesController = new BattlesController(new UnitOfWork());
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

        if (request.getMethod() == Method.POST) {
            String username = AuthenticationFilter.getUsername(request);

            if (username == null) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\": \"Access token ist ungültig\" }"
                );
            }

            return battlesController.startBattle(username);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "{ \"message\": \"Ungültige Anfrage\" }"
        );
    }
}
