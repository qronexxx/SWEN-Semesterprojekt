package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.Package;
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

    public void buyPackages(String username) {
        try {
            // User vorab laden und prüfen, ob genug Coins vorhanden
            User user = new UserRepository(unitOfWork).findUserbyUsername(username);
            if(user == null) {
                throw new DataAccessException("User not found");
            }
            if(user.getCoins() < 5) {
                throw new DataAccessException("Not enough coins");
            }

            // Erstes Package abfragen
            PreparedStatement psSelect = unitOfWork.prepareStatement("""
                SELECT PackageID, Card1, Card2, Card3, Card4, Card5
                FROM Packages
                ORDER BY PackageID
                LIMIT 1
            """);
            ResultSet rs = psSelect.executeQuery();
            Integer packageId = null;
            ArrayList<String> cards = new ArrayList<>();
            if(rs.next()) {
                packageId = rs.getInt("PackageID");
                cards.add(rs.getString("Card1"));
                cards.add(rs.getString("Card2"));
                cards.add(rs.getString("Card3"));
                cards.add(rs.getString("Card4"));
                cards.add(rs.getString("Card5"));
            } else {
                // Kein Package vorhanden
                throw new DataAccessException("No package available");
            }

            // Karten in UserStacks einfügen
            PreparedStatement psInsertStack = unitOfWork.prepareStatement("""
            INSERT INTO UserStacks(Username, CardID) VALUES (?, ?)
        """);
            for(String cardId : cards) {
                psInsertStack.setString(1, username);
                psInsertStack.setObject(2, UUID.fromString(cardId));
                psInsertStack.addBatch();
            }
            psInsertStack.executeBatch();

            // Package löschen
            PreparedStatement psDeletePackage = unitOfWork.prepareStatement("""
                DELETE FROM Packages WHERE PackageID = ?
                """);
            psDeletePackage.setInt(1, packageId);
            psDeletePackage.executeUpdate();

            // User Coins -5
            PreparedStatement psUpdateCoins = unitOfWork.prepareStatement("""
                UPDATE Users
                SET Coins = Coins - 5
                WHERE Username = ?
            """);
            psUpdateCoins.setString(1, username);
            psUpdateCoins.executeUpdate();

        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new DataAccessException("Acquire Package failed", e);
        }
    }
}
