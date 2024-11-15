package at.technikum_wien.app.controllers;

import at.technikum_wien.app.business.UserDummyDAL;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SessionController extends Controller {
    // POST /sessions
    public Response handleLogin(Request request) {
        try {
            User credentialsFromRequest = this.getObjectMapper().readValue(request.getBody(), User.class);

            for (User user : UserDummyDAL.getUserData()) {
                if (user.getUsername().equals(credentialsFromRequest.getUsername()) &&
                        user.getPassword().equals(credentialsFromRequest.getPassword())) {

                    // if user found
                    String token = user.getUsername() + "-mtcgToken";
                    return new Response(
                            HttpStatus.OK,
                            ContentType.JSON,
                            "{ \"token\": \"" + token + "\" }"
                    );
                }
            }

            // user not found or invalid password
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"Invalid username/password provided\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
