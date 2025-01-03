package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
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
                System.out.println(new ObjectMapper().writeValueAsString(user));
                userRows.add(user);
            }

            return userRows;
        } catch (SQLException e) {
            throw new DataAccessException("Select failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
                System.out.println(new ObjectMapper().writeValueAsString(user));
            }

            return user;
        } catch (SQLException e) {
            throw new DataAccessException("Select failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            System.out.println(new ObjectMapper().writeValueAsString(user));
            if(affectedRows > 0){
                return user;
            }else{
                throw new DataAccessException("Insert nicht erfolgreich, keine Zeilen betroffen.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser(User user) {
        try(PreparedStatement preparedStatement =
                    this.unitOfWork.prepareStatement("""
                insert into users(username, password) 
                VALUES (?, ?);
            """))
        {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());


            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(new ObjectMapper().writeValueAsString(user));
            if(affectedRows > 0){
                return user;
            }else{
                throw new DataAccessException("Insert nicht erfolgreich, keine Zeilen betroffen.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
