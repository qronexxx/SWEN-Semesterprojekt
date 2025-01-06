package at.technikum_wien.app.controllers;

import at.technikum_wien.app.business.BattleField;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BattlesController extends Controller {
    private static final ConcurrentLinkedQueue<User> lobby = new ConcurrentLinkedQueue<>();
    private final UnitOfWork unitOfWork;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public BattlesController(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
        this.userRepository = new UserRepository(unitOfWork);
        this.objectMapper = new ObjectMapper();
    }

    public Response startBattle(String username) {
        try {
            User currentUser = userRepository.findUserByUsername(username);
            if (currentUser == null) {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{ \"message\": \"User not found\" }");
            }

            User opponent = lobby.poll();
            if (opponent == null) {
                lobby.add(currentUser);
                return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\": \"Warte auf einen Gegner...\" }");
            }

            System.out.println(currentUser.getUsername() + opponent.getUsername());
            BattleField battleField = new BattleField(currentUser, opponent);
            String result = battleField.startBattle();


            System.out.println("Battle Result: " + result);
            System.out.println("Battle Log:");
            for (String log : battleField.getBattleLog()) {
                System.out.println(log);
            }

            // Aktualisiere User-Statistiken
            userRepository.updateUserStats(currentUser);
            userRepository.updateUserStats(opponent);
            unitOfWork.commitTransaction();

            String battleLogJson = objectMapper.writeValueAsString(battleField.getBattleLog());
            return new Response(HttpStatus.OK, ContentType.JSON, battleLogJson);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Serverfehler\" }");
        }
    }
}
