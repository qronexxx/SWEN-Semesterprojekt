package at.technikum_wien.app.controllers;


import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.app.dto.UserDTO;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Request;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.Collection;
import java.util.List;

public class UserController extends Controller{

    public UserController() {
        // Nur noch für die Dummy-JUnit-Tests notwendig. Stattdessen ein RepositoryPattern verwenden.

    }

    // GET /users
    public Response getAllUsersPerRepository() {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            Collection<User> userData = new UserRepository(unitOfWork).findAllUser();
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);
            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userDataJSON
            );
        } catch (Exception e) {
            e.printStackTrace();

            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    // GET /users(:username
    public Response getUserPerRepository(String Username){
        UnitOfWork unitOfWork = new UnitOfWork();
        try{
            User userData = new UserRepository(unitOfWork).findUserbyUsername(Username);
            if(userData == null){
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"User not found\" }"
                );
            }

            String userJSON = this.getObjectMapper().writeValueAsString(userData);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userJSON
            );
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    public Response getUserDataPerUsername(String Username){
        UnitOfWork unitOfWork = new UnitOfWork();
        try{
            UserDTO userData = new UserRepository(unitOfWork).giveUserdata(Username);
            if(userData == null){
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"User not found\" }"
                );
            }

            String userJSON = this.getObjectMapper().writeValueAsString(userData);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userJSON
            );
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    // POST /users
    public Response addUser(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);

            if(userRepository.findUserbyUsername(user.getUsername()) != null){
                // Return 409 Conflict if user already exists
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\": \"User with this username already exists\" }"
                );
            }

            userRepository.createUser(user);
            unitOfWork.commitTransaction();

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ message: \"User successfully created\" }"
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

    // PUT /users
    public Response updateUser(String Username, Request request) throws Exception {
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        try {
            // Parse das Request-Body JSON in ein UpdateUserDTO
            UserDTO updateData = this.getObjectMapper().readValue(request.getBody(), UserDTO.class);

            // Aktualisiere den Benutzer in der Datenbank
            userRepository.updateUserInRepo(Username, updateData.getName(), updateData.getBio(), updateData.getImage());

            // Holt die aktualisierten Benutzerdaten
            UserDTO updatedUser = userRepository.giveUserdata(Username);
            if (updatedUser == null) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"User not found after update\" }"
                );
            }

            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ \"message\": \"Update successful\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            System.out.println(e.getMessage());
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"JSON processing error\" }"
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"Database error\" }"
            );
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\": \"Server error\" }"
            );
        } finally {
            unitOfWork.close();
        }
    }
}