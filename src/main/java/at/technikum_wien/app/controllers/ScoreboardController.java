package at.technikum_wien.app.controllers;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.StatsRepository;
import at.technikum_wien.app.dto.UserStatsDTO;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class ScoreboardController extends Controller {
    private final StatsRepository statsRepository;

    public ScoreboardController(UnitOfWork unitOfWork) {
        this.statsRepository = new StatsRepository(unitOfWork);
    }

    public Response showScoreboard() {
        try {
            List<UserStatsDTO> scoreboard = statsRepository.getScoreboard();
            String scoreboardJSON = this.getObjectMapper().writeValueAsString(scoreboard);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    scoreboardJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
}