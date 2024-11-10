package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.controllers.UserController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

public class UserService implements Service {
    private final UserController userController;

    public UserService(){
        this.userController = new UserController();
    }

    @Override
    public Response handleRequest(Request request){
        if (request.getMethod() == Method.GET &&
                request.getPathParts().size() > 1) {
            return this.userController.getUser(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.GET) {
            return this.userController.getUsersPerRepository();
        } else if (request.getMethod() == Method.POST) {
            return this.userController.addUser(request);
        }else if (request.getMethod() == Method.PUT) {
            return this.userController.addUser(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
