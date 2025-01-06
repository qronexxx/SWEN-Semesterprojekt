package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.TradingsController;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;


public class TradingsService implements Service {
    private final TradingsController tradingsController;

    public TradingsService() {
        this.tradingsController = new TradingsController(new UnitOfWork());
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

        if (request.getMethod() == Method.POST &&
                request.getPathParts().size() > 1) {
            return tradingsController.carryOutTrade(request.getPathParts().get(1), request, username);
        }else if (request.getMethod() == Method.POST) {
            return tradingsController.createDeal(request, username);
        }else if (request.getMethod() == Method.DELETE &&
                request.getPathParts().size() > 1) {
            return this.tradingsController.deleteTrading(request.getPathParts().get(1), username);
        }else if (request.getMethod() == Method.GET) {
            return tradingsController.getAllDeals();
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "{ \"message\": \"Bad request\" }"
        );
    }
}
