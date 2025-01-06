package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.dto.UserDTO;
import at.technikum_wien.app.dto.UserStatsDTO;
import at.technikum_wien.app.modles.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsRepository {
    private UnitOfWork unitOfWork;

    public StatsRepository(UnitOfWork unitOfWork) {this.unitOfWork = unitOfWork;}

    public UserStatsDTO giveUserStats(String username) {
        UserStatsDTO userStatsDTO = null;
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                    SELECT name, elo, wins, losses FROM users
                    WHERE username = ?
                """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userStatsDTO = new UserStatsDTO(
                        resultSet.getString("name"),
                        resultSet.getInt("elo"),
                        resultSet.getInt("wins"),
                        resultSet.getInt("losses")
                );

                // Optional: Serialize UserDTO für das Terminal
                String userJSON = new ObjectMapper().writeValueAsString(userStatsDTO);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return userStatsDTO;
    }

    public List<UserStatsDTO> getScoreboard() throws DataAccessException {
        List<UserStatsDTO> scoreboard = new ArrayList<>();
        String sql = "SELECT Name, Elo, Wins, Losses FROM users ORDER BY Elo DESC";

        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserStatsDTO userStats = new UserStatsDTO(
                            rs.getString("Name"),
                            rs.getInt("Elo"),
                            rs.getInt("Wins"),
                            rs.getInt("Losses")
                    );
                    scoreboard.add(userStats);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving scoreboard", e);
        }

        return scoreboard;
    }
}
