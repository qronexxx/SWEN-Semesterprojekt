package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserDummyDAL {
    // Mache die userData-Liste static, damit sie für alle Instanzen zugänglich ist
    @Getter
    private static List<User> userData = new ArrayList<>();

    public UserDummyDAL() {
        // Initialisiere die Liste nur einmal, falls sie leer ist
        if (userData.isEmpty()) {
            userData.add(new User("Elias", "123"));
            userData.add(new User("Joel", "123"));
            userData.add(new User("Darko", "123"));
            userData.add(new User("Roy", "123"));
            //userData.add(new User("admin", "admin", true));
        }
    }

    public User getUser(String username) {
        return userData.stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny()
                .orElse(null);
    }

    public void addUser(User user) {
        userData.add(user);
    }
}
