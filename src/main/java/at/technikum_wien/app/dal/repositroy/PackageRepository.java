package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;



public class PackageRepository {
    private UnitOfWork unitOfWork;
    public PackageRepository(UnitOfWork unitOfWork) {this.unitOfWork = unitOfWork;}

    public void createPackage(Card[] cards) {
        try {
            // 1) Für jede Karte prüfen, ob sie in der Cards-Tabelle existiert.
            //    Wenn nicht, anlegen (bei Konflikt Exception werfen oder ON CONFLICT-Strategie verwenden).
            String cardInsertSql = """
            INSERT INTO Cards (CardID, Name, Damage)
            VALUES (?, ?, ?)
            ON CONFLICT (CardID) DO NOTHING
        """;
            try (PreparedStatement ps = unitOfWork.prepareStatement(cardInsertSql)) {
                for (Card card : cards) {
                    ps.setObject(1, UUID.fromString(card.getId()));
                    ps.setString(2, card.getName());
                    ps.setDouble(3, card.getDamage());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 2) Package-Eintrag erstellen: Fünf CardIDs in Packages-Tabelle eintragen
            String packageInsertSql = """
            INSERT INTO Packages (Card1, Card2, Card3, Card4, Card5)
            VALUES (?, ?, ?, ?, ?)
        """;
            try (PreparedStatement ps = unitOfWork.prepareStatement(packageInsertSql)) {
                ps.setObject(1, UUID.fromString(cards[0].getId()));
                ps.setObject(2, UUID.fromString(cards[1].getId()));
                ps.setObject(3, UUID.fromString(cards[2].getId()));
                ps.setObject(4, UUID.fromString(cards[3].getId()));
                ps.setObject(5, UUID.fromString(cards[4].getId()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DataAccessException("Package creation failed", e);
        }
    }
}
