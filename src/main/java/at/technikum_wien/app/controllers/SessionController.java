package at.technikum_wien.app.controllers;


import at.technikum_wien.app.business.TokenManager;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SessionController extends Controller {
    // POST /sessions
    private final TokenManager tokenManager = TokenManager.getInstance();
    UnitOfWork unitOfWork = new UnitOfWork();
    UserRepository userRepository = new UserRepository(unitOfWork);


    public Response handleLogin(Request request) {
        try {
            User credentialsFromRequest = this.getObjectMapper().readValue(request.getBody(), User.class);
            User sessionUser = userRepository.findUserByUsername(credentialsFromRequest.getUsername());



            if(credentialsFromRequest.getUsername().equals(sessionUser.getUsername()) && credentialsFromRequest.getPassword().equals(sessionUser.getPassword())) {
                // if user found
                String token = credentialsFromRequest.getUsername() + "-mtcgToken";
                tokenManager.addToken(token, sessionUser.getUsername());

                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"token\": \"" + token + "\" }"
                );
            }
            // user not found or invalid password
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"Login failed\"}\" }"
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
