package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.CardRepository;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.app.dal.repositroy.TradingRepository;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.TradeRequest;
import at.technikum_wien.app.modles.TradingDeal;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.List;
import java.util.UUID;

public class TradingsController extends Controller {

    private TradingRepository tradingRepository;
    private UserRepository userRepository;
    private CardRepository cardRepository;

    public TradingsController(UnitOfWork unitOfWork) {
        this.tradingRepository = new TradingRepository(unitOfWork);
        this.userRepository = new UserRepository(unitOfWork);
        this.cardRepository = new CardRepository(unitOfWork);
    }

    public Response createDeal(Request request, String username) {
        try {
            // Parse TradingDeal from request body
            TradingDeal deal = getObjectMapper().readValue(request.getBody(), TradingDeal.class);

            // Überprüfen, ob DealID bereits existiert
            if (tradingRepository.exists(UUID.fromString(deal.getId()))) {
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{ \"message\": \"Deal ID already exists.\" }");
            }

            // Überprüfen, ob der Benutzer die Karte besitzt
            if (!cardRepository.isCardOwnedByUser(UUID.fromString(deal.getCardToTrade()), username)) {
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{ \"message\": \"Card not owned by user or locked in deck.\" }");
            }

            // Einfügen des Handelsangebots
            tradingRepository.createDeal(deal, username);

            return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\": \"Trading deal successfully created.\" }");

        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"Invalid JSON format.\" }");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error.\" }");
        }
    }

    public Response getAllDeals() {
        try {
            List<TradingDeal> deals = tradingRepository.getAllDeals();
            String jsonResponse = getObjectMapper().writeValueAsString(deals);
            return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error.\" }");
        } catch (DataAccessException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Database error.\" }");
        }
    }

    public Response deleteTrading(String tradingDealId, String username) {
        try {
            UUID dealId = UUID.fromString(tradingDealId);
            TradingDeal deal = tradingRepository.getDealById(dealId);

            if (deal == null) {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{ \"message\": \"Deal not found.\" }");
            }

            // Überprüfen, ob der Benutzer die zugehörige Karte besitzt
            UUID cardId = UUID.fromString(deal.getCardToTrade());
            boolean ownsCard = tradingRepository.isCardOwnedByUser(cardId, username);

            if (!ownsCard) {
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{ \"message\": \"You do not own the associated card.\" }");
            }

            // Löschen des Handelsangebots
            tradingRepository.deleteDeal(dealId);
            return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\": \"Trading deal deleted successfully.\" }");

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"Invalid UUID format.\" }");
        } catch (DataAccessException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error.\" }");
        }
    }

    public Response carryOutTrade(String tradingDealId, Request request, String username) {
        try {
            // Read the offered card ID directly from the request body
            String body = request.getBody().trim();

            // Remove surrounding quotes if present
            if (body.startsWith("\"") && body.endsWith("\"")) {
                body = body.substring(1, body.length() - 1);
            }

            // Parse the offered card UUID
            UUID offeredCardUUID = UUID.fromString(body);
            UUID dealId = UUID.fromString(tradingDealId);

            // get deal
            TradingDeal deal = tradingRepository.getDealById(dealId);
            if (deal == null) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"Trade deal not found.\" }"
                );
            }

            String dealOwner = deal.getUsername();

            // check if user trades with himself
            if (dealOwner.equalsIgnoreCase(username)) {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\": \"Cannot trade with yourself.\" }"
                );
            }

            // Execute the trade
            tradingRepository.executeTrade(UUID.fromString(tradingDealId), offeredCardUUID, username);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ \"message\": \"Trade completed successfully.\" }"
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "{ \"message\": \"Invalid UUID format.\" }"
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"Server error: " + e.getMessage() + "\" }"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "{ \"message\": \"Invalid input.\" }"
            );
        }
    }
}
