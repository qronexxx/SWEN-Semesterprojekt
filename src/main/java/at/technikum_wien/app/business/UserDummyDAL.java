package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserDummyDAL {
    private List<User> userData;

    public UserDummyDAL(){
        userData = new ArrayList<>();
        userData.add(new User(1, "Elias", "123", 100, 100));
        userData.add(new User(2, "Joel", "123", 50, 110));
        userData.add(new User(3, "Darko", "123", 75, 85));
        userData.add(new User(4, "Roy", "123", 25, 70));
    }

    public User getUser(Integer ID){
        User foundUser = userData.stream()
                .filter(user -> ID == user.getUserID())
                .findAny()
                .orElse(null);
        return foundUser;
    }

    public List<User> getUserData() {
        return userData;
    }

    public void addUser(User user){
        userData.add(user);
    }
}
