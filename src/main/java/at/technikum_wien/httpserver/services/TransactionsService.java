package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.controllers.TransactionsController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class TransactionsService implements Service {
    private final TransactionsController transactionsController;

    public TransactionsService() {
        this.transactionsController = new TransactionsController();
    }

    @Override
    public Response handleRequest(Request request){
        if (request.getMethod() == Method.POST) {
            return this.transactionsController.acquirePackage();
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
