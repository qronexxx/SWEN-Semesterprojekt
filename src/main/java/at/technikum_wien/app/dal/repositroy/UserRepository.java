package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dto.UserDTO;
import at.technikum_wien.app.modles.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepository(UnitOfWork unitOfWork) {this.unitOfWork = unitOfWork;}

    public Collection<User> findAllUser() {
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                    select * from users
                """))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();

            while(resultSet.next())
            {
                User user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                user.setCoins(resultSet.getInt("coins"));
                user.setElo(resultSet.getInt("elo"));
                userRows.add(user);
            }

            return userRows;
        } catch (SQLException e) {
            throw new DataAccessException("Select failed", e);
        }
    }

    public User findUserbyUsername(String username) {
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                    select * from users
                    WHERE username = ?
                """))
        {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;

            while(resultSet.next())
            {
                user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                user.setCoins(resultSet.getInt("coins"));
                user.setElo(resultSet.getInt("elo"));
            }

            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Select failed", e);
        }
    }

    public User createUser(User user) {
        try(PreparedStatement preparedStatement =
                this.unitOfWork.prepareStatement("""
                insert into users(username, password) 
                VALUES (?, ?);
            """))
        {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());


            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                return user;
            }else{
                throw new DataAccessException("Insert nicht erfolgreich, keine Zeilen betroffen.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert failed", e);
        }
    }

    public UserDTO giveUserdata(String username) {
        UserDTO userDTO = null;
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                    SELECT name, bio, image FROM users
                    WHERE username = ?
                """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userDTO = new UserDTO(
                        resultSet.getString("name"),
                        resultSet.getString("bio"),
                        resultSet.getString("image")
                );

                // Optional: Serialize UserDTO für das Terminal
                String userJSON = new ObjectMapper().writeValueAsString(userDTO);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return userDTO;
    }

    public void updateUserInRepo(String username, String name, String bio, String image) {
        String updateSQL = """
                UPDATE users
                SET Name = ?, Bio = ?, Image = ?
                WHERE username = ?
            """;

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, bio);
            preparedStatement.setString(3, image);
            preparedStatement.setString(4, username);

            int affectedRows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Update fehlgeschlagen", e);
        }
    }
}
