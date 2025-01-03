package at.technikum_wien.app.controllers;

import at.technikum_wien.app.business.TokenManager;
import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.PackageRepository;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import static at.technikum_wien.app.business.AuthenticationFilter.tokenManager;

public class PackagesController extends Controller {
    UnitOfWork unitOfWork = new UnitOfWork();
    UserRepository userRepository = new UserRepository(unitOfWork);

    public Response createPackage(Request request) {
        try (UnitOfWork unitOfWork = new UnitOfWork()) {
            String authHeader = request.getHeaderMap().getHeader("Authorization");
            // TokenManager liefert uns z.B. den Usernamen
            String token = authHeader.substring(7);
            System.out.println(token);
            String username = tokenManager.getUsernameForToken(token);
            System.out.println(username);


            // Prüfen, ob Admin
            User user = userRepository.findUserbyUsername(username);;
            if(!"admin".equals(user.getUsername())) {
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{ \"message\": \"Not allowed\"}");
            }

            // JSON zu Card[] parsen
            Card[] cards = getObjectMapper().readValue(request.getBody(), Card[].class);
            if (cards.length != 5) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"Exactly 5 cards required\"}");
            }

            // Karten anlegen und Package-Eintrag erzeugen
            PackageRepository packagesRepo = new PackageRepository(unitOfWork);
            packagesRepo.createPackage(cards);  // wirft ggf. Exception bei Konflikten

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\": \"Package created\"}");
        }
        catch(JsonProcessingException e) {
            System.out.println(e.getMessage());
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{ \"message\": \"Invalid JSON\"}");
        }
        catch(DataAccessException e) {
            System.out.println(e.getMessage());
            return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{ \"message\": \"Card conflict\"}");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error\"}");
        }
    }
}
