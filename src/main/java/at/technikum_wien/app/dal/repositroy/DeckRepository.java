package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.Deck;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DeckRepository {
    private final UnitOfWork unitOfWork;

    public DeckRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void setUserDeck(String username, List<String> cardIds) throws DataAccessException {
        if (cardIds.size() != 4) {
            throw new DataAccessException("Exactly 4 cards must be provided.");
        }

        // Check if the user already has a deck
        String checkSql = "SELECT COUNT(*) AS cnt FROM UserDecks WHERE Username = ?";
        try (PreparedStatement psCheck = unitOfWork.prepareStatement(checkSql)) {
            psCheck.setString(1, username);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    throw new DataAccessException("Deck already exists. Cannot overwrite.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking existing deck.", e);
        }

        // Create a new deck
        String insertSql = "INSERT INTO UserDecks (Username, CardPosition, CardID) VALUES (?, ?, ?)";
        try (PreparedStatement psInsert = unitOfWork.prepareStatement(insertSql)) {

            for (int i = 0; i < cardIds.size(); i++) {
                psInsert.setString(1, username);
                psInsert.setInt(2, i + 1);
                psInsert.setObject(3, UUID.fromString(cardIds.get(i)));
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new DataAccessException("Error setting user deck.", e);
        }
    }

    public Deck getUserDeck(String username) throws DataAccessException {
        Deck deck = new Deck();
        String sql = """
            SELECT CardID, CardPosition
            FROM UserDecks
            WHERE Username = ?
            ORDER BY CardPosition
        """;

        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("CardID");
                    Card card = getCardById(cardId);
                    deck.addCard(card);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error loading deck", e);
        }

        return deck;
    }

    private Card getCardById(String cardId) throws DataAccessException {
        String sql = """
            SELECT CardID, Name, Damage
            FROM Cards
            WHERE CardID = ?
        """;

        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(cardId));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("CardID"));
                    String name = rs.getString("Name");
                    int damage = rs.getInt("Damage");
                    return new Card(id, name, damage);
                } else {
                    throw new DataAccessException("Card not found: " + cardId);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving card", e);
        }
    }
}
