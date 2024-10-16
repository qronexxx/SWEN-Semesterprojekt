package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.controllers.TradingsController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class TradingsService implements Service {
    private final TradingsController tradingsController;

    public TradingsService() {
        this.tradingsController = new TradingsController();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return this.tradingsController.availableTradings();
        } else if (request.getMethod() == Method.DELETE &&
                request.getPathParts().size() > 1) {
            return this.tradingsController.deleteTrading(request.getPathParts().get(1));
        }else if (request.getMethod() == Method.POST &&
                request.getPathParts().size() > 1) {
            return this.tradingsController.carryOutTrade(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.POST) {
            return this.tradingsController.createDeal();
        }


        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
