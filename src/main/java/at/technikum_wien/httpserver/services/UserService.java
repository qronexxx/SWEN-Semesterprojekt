package at.technikum_wien.httpserver.services;

import at.technikum_wien.app.business.AuthenticationFilter;
import at.technikum_wien.app.controllers.UserController;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.http.Method;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import at.technikum_wien.httpserver.server.Service;

import static at.technikum_wien.app.business.AuthenticationFilter.isAuthenticated;
import static at.technikum_wien.app.business.AuthenticationFilter.tokenManager;

public class UserService implements Service {
    private final UserController userController;

    public UserService(){
        this.userController = new UserController();
    }

    @Override
    public Response handleRequest(Request request) throws Exception {
        if (request.getMethod() == Method.GET && request.getPathParts().size() > 1) {
            Response authResponse = authenticateAndAuthorize(request);
            if (authResponse != null) {
                return authResponse;
            }else{
                return this.userController.getUserDataPerUsername(request.getPathParts().get(1));
            }
        } else if (request.getMethod() == Method.GET) {
            return this.userController.getAllUsersPerRepository();
        } else if (request.getMethod() == Method.POST) {
            return this.userController.addUser(request);
        }else if (request.getMethod() == Method.PUT) {
            Response authResponse = authenticateAndAuthorize(request);
            if (authResponse != null) {
                return authResponse;
            }else{
                return this.userController.updateUser(request.getPathParts().get(1), request);
            }
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response authenticateAndAuthorize(Request request) {
        if (!AuthenticationFilter.isAuthenticated(request)){
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"not logged in\" }"
            );
        }

        String tokenUsername = AuthenticationFilter.getUsername(request);
        String urlUsername = request.getPathParts().get(1);

        if (!tokenUsername.equals(urlUsername) && !tokenUsername.equals("admin")){
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"user and token not matching\" }"
            );
        }

        return null; // Zugriff erlaubt
    }
}
