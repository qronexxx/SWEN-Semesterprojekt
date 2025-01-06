package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.StatsRepository;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.app.dto.UserStatsDTO;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class StatsController extends Controller {

    public Response getUserStats(String Username){
        UnitOfWork unitOfWork = new UnitOfWork();
        try{
            UserStatsDTO userData = new StatsRepository(unitOfWork).giveUserStats(Username);
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
}
