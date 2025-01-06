package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.DeckRepository;
import at.technikum_wien.app.modles.Deck;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;


public class DeckController extends Controller {

    private DeckRepository deckRepository;

    public DeckController(UnitOfWork unitOfWork) {
        this.deckRepository = new DeckRepository(unitOfWork);
    }


     //GET /decks/:username

    public Response getUserDeck(String username) {
        try {
            Deck deck = deckRepository.getUserDeck(username);
            String json = this.getObjectMapper().writeValueAsString(deck);
            return new Response(HttpStatus.OK, ContentType.JSON, json);
        } catch (DataAccessException | JsonProcessingException e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error\" }");
        }
    }


     //OST /decks/:username

    public Response setUserDeck(String username, Request request) {
        try {
            // List with CardIDs in JSON
            List<String> cardIds = this.getObjectMapper().readValue(request.getBody(), ArrayList.class);

            deckRepository.setUserDeck(username, cardIds);
            return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\": \"Deck created/updated\" }");
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"Invalid JSON\" }");
        } catch (DataAccessException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"" + e.getMessage() + "\" }");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error\" }");
        }
    }
}
