package at.technikum_wien.app.controllers;

import at.technikum_wien.app.business.BattleField;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dal.repositroy.UserRepository;
import at.technikum_wien.app.modles.User;
import at.technikum_wien.httpserver.http.ContentType;
import at.technikum_wien.httpserver.http.HttpStatus;
import at.technikum_wien.httpserver.server.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.*;

public class BattlesController extends Controller {
    private static final ConcurrentLinkedQueue<User> lobby = new ConcurrentLinkedQueue<>();
    private static final Object lobbyLock = new Object(); // Lock object for synchronization
    private static final ExecutorService battleExecutor = Executors.newCachedThreadPool();
    private static final Map<String, String> battleResults = new ConcurrentHashMap<>();

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

            synchronized (lobbyLock) {
                User opponent = lobby.poll();
                if (opponent == null) {
                    lobby.add(currentUser);
                    return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\": \"Waiting for opponent\" }");
                }

                // Start battle in a separate thread
                battleExecutor.submit(() -> executeBattle(currentUser, opponent));
                return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\": \"Battle started\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\": \"Server error\" }");
        }
    }

    private void executeBattle(User user1, User user2) {
        BattleField battleField = new BattleField(user1, user2);
        String result = battleField.startBattle();

        System.out.println("Battle Result: " + result);
        battleField.getBattleLog().forEach(System.out::println);

        // Update user statistics
        userRepository.updateUserStats(user1);
        userRepository.updateUserStats(user2);
        unitOfWork.commitTransaction();

        try {
            String battleLogJson = objectMapper.writeValueAsString(battleField.getBattleLog());
            String battleWinner = battleField.getBattleWinner();

            if (battleWinner.equals(user1.getUsername())) {
                battleResults.put(user1.getUsername(), "You won against " + user2.getUsername() + "!");
                battleResults.put(user2.getUsername(), user1.getUsername() + " won against you!");
            } else if (battleWinner.equals(user2.getUsername())) {
                battleResults.put(user1.getUsername(), user2.getUsername() + " won against you!");
                battleResults.put(user2.getUsername(), "You won against  " + user1.getUsername() + "!");
            } else {
                battleResults.put(user1.getUsername(), "The battle ended in a draw!");
                battleResults.put(user2.getUsername(), "The battle ended in a draw!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBattleResult(String username) {
        return battleResults.remove(username);
    }

    // Method to shut down the ExecutorService
    public void shutdown() {
        battleExecutor.shutdown();
    }
}