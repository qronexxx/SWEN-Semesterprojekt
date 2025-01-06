package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dto.UserDTO;
import at.technikum_wien.app.modles.*;
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

    public User findUserByUsername(String username) throws DataAccessException {
        String sql = "SELECT username, password, name, coins, elo, wins, losses, bio, image FROM users WHERE username = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getInt("coins"),
                        rs.getInt("elo"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getString("bio"),
                        rs.getString("image")
                );
                loadUserDeck(user);
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching user", e);
        }
    }

    private void loadUserDeck(User user) throws SQLException, DataAccessException {
        String sql = """
            SELECT c.CardID, c.Name, c.Damage
            FROM UserDecks ud
            JOIN Cards c ON ud.CardID = c.CardID
            WHERE ud.Username = ?
            ORDER BY ud.CardPosition
        """;
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            Deck deck = new Deck();
            while (rs.next()) {
                String cardName = rs.getString("Name");
                int damage = rs.getInt("Damage");
                Card card = new Card(cardName, damage);
                deck.addCard(card);
            }
            user.setDeck(deck);
        }
    }

    public void updateUserStats(User user) throws DataAccessException {
        String sql = "UPDATE users SET Elo = ?, Wins = ?, Losses = ? WHERE Username = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setInt(1, user.getElo());
            ps.setInt(2, user.getWins());
            ps.setInt(3, user.getLosses());
            ps.setString(4, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user stats", e);
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
                throw new DataAccessException("no lines affected from insert");
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
            throw new DataAccessException("update failed", e);
        }
    }
}
