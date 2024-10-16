package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.controllers.PackagesController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class PackagesService implements Service {
    private final PackagesController packagesController;

    public PackagesService() {
        this.packagesController = new PackagesController();
    }

    @Override
    public Response handleRequest(Request request){
        if (request.getMethod() == Method.POST) {
            return this.packagesController.createPackage();
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
