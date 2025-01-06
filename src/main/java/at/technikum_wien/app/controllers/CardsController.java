package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.CardRepository;
import at.technikum_wien.app.modles.Stack;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;


public class CardsController extends Controller {
    private CardRepository cardRepository;

    public CardsController(UnitOfWork unitOfWork) {
        this.cardRepository = new CardRepository(unitOfWork);
    }

    // GET /cards/:username
    public Response getUserCards(String username) {
        try {
            Stack userStack = cardRepository.getCards(username);
            String json = this.getObjectMapper().writeValueAsString(userStack);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    json
            );
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"Server error\" }"
            );
        }
    }
}
