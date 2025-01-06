package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.Stack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CardRepository {
    private UnitOfWork unitOfWork;
    public CardRepository(UnitOfWork unitOfWork) {this.unitOfWork = unitOfWork;}

    public Stack getCards(String username) throws SQLException {
        Stack stack = new Stack();
        String sql = """
            SELECT c.CardID, c.Name, c.Damage
            FROM UserStacks us
            JOIN Cards c ON us.CardID = c.CardID
            WHERE us.Username = ?
        """;

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID cardId = UUID.fromString(resultSet.getString("CardID"));
                    String name = resultSet.getString("Name");
                    int damage = resultSet.getInt("Damage");

                    Card card = new Card(cardId, name, damage);
                    stack.getCards().add(card);
                }
            }
        }

        return stack;
    }

    public boolean isCardOwnedByUser(UUID cardId, String username) throws DataAccessException {
        String sql = """
        SELECT 1 FROM UserStacks us
        LEFT JOIN UserDecks ud ON us.Username = ud.Username AND us.CardID = ud.CardID
        WHERE us.CardID = ? AND us.Username = ? AND ud.CardID IS NULL
    """;
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, cardId);
            ps.setString(2, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking card owner.", e);
        }
    }

    public Card getCardById(UUID cardId) throws DataAccessException {
        String sql = "SELECT CardID, Name, Damage FROM Cards WHERE CardID = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("CardID");
                    String name = rs.getString("Name");
                    int damage = rs.getInt("Damage");
                    return new Card(UUID.fromString(id), name, damage);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching card by ID.", e);
        }
    }
}


