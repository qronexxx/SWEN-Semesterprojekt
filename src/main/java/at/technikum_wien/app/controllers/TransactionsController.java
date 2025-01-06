package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.PackageRepository;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;

import static at.technikum_wien.app.business.AuthenticationFilter.tokenManager;


public class TransactionsController extends Controller {
    UnitOfWork unitOfWork = new UnitOfWork();
    UserRepository userRepository = new UserRepository(unitOfWork);

    public Response acquirePackage(Request request) {
        try {
            String authHeader = request.getHeaderMap().getHeader("Authorization");
            // get username with Token manager
            String token = authHeader.substring(7);
            String username = tokenManager.getUsernameForToken(token);

            PackageRepository packagesRepo = new PackageRepository(unitOfWork);
            packagesRepo.buyPackages(username);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\": \"Package aquired!\" }\"}");
        }
        catch(DataAccessException e) {
            unitOfWork.rollbackTransaction();
            String errorMessage = e.getMessage();
            HttpStatus status;

            switch(errorMessage) {
                case "User not found":
                    status = HttpStatus.NOT_FOUND;
                    break;
                case "Not enough coins":
                    status = HttpStatus.FORBIDDEN; // 403 Forbidden
                    break;
                case "No package available":
                    status = HttpStatus.NOT_FOUND;
                    break;
                default:
                    status = HttpStatus.BAD_REQUEST;
            }

            return new Response(
                    status,
                    ContentType.JSON,
                    "{ \"message\": \"" + errorMessage + "\" }"
            );
        }
        catch(Exception e) {
            unitOfWork.rollbackTransaction();
            System.out.println(e.getMessage());
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"Server error\" }"
            );
        }
    }
}
