package at.technikum_wien.app.business;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenManager {
    private static final TokenManager instance = new TokenManager();
    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();

    private TokenManager() {}

    public static TokenManager getInstance() {
        return instance;
    }

    public void addToken(String token, String username) {
        tokenToUsername.put(token, username);
    }

    public String getUsernameForToken(String token) {
        return tokenToUsername.get(token);
    }

    public boolean isValidToken(String token) {
        return tokenToUsername.containsKey(token);
    }

    public void removeToken(String token) {
        tokenToUsername.remove(token);
    }

    public void listTokens() {
        for (String token : tokenToUsername.keySet()) {
            System.out.println(token);
        }
    }
}