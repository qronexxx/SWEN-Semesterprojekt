package at.technikum_wien.app.dal.repositroy;

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
}


