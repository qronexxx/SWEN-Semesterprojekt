package at.technikum_wien.app.dal.repositroy;

import at.technikum_wien.app.dal.DataAccessException;
import at.technikum_wien.app.dal.UnitOfWork;
import at.technikum_wien.app.modles.TradingDeal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradingRepository {
    private final UnitOfWork unitOfWork;

    public TradingRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    // check if trading deal with that id exists
    public boolean exists(UUID dealId) throws DataAccessException {
        String sql = "SELECT 1 FROM TradingDeals WHERE DealID = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, dealId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking deal existence.", e);
        }
    }

    public TradingDeal getDealById(UUID dealId) throws DataAccessException {
        String sql = "SELECT * FROM TradingDeals WHERE DealID = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, dealId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TradingDeal deal = new TradingDeal();
                deal.setId(rs.getString("DealID"));
                deal.setUsername(rs.getString("Username"));
                deal.setCardToTrade(rs.getString("CardToTrade"));
                deal.setType(rs.getString("Type"));
                deal.setMinimumDamage(rs.getBigDecimal("MinimumDamage").doubleValue());
                return deal;
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching trading deal by ID.", e);
        }
    }

    public void deleteDeal(UUID dealId) throws DataAccessException {
        String sql = "DELETE FROM TradingDeals WHERE DealID = ?";
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, dealId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("No trading deal found to delete.");
            }
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new DataAccessException("Error deleting trading deal.", e);
        }
    }

    // check if card is owned by user and is not in the deck
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
            throw new DataAccessException("Error verifying card ownership.", e);
        }
    }

    public void createDeal(TradingDeal deal, String username) throws DataAccessException {
        String sql = """
            INSERT INTO TradingDeals (DealID, Username, CardToTrade, Type, MinimumDamage)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(deal.getId()));
            ps.setString(2, username);
            ps.setObject(3, UUID.fromString(deal.getCardToTrade()));
            ps.setString(4, deal.getType());
            ps.setBigDecimal(5, new java.math.BigDecimal(deal.getMinimumDamage()));
            ps.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating trading deal.", e);
        }
    }

    public List<TradingDeal> getAllDeals() throws DataAccessException {
        String sql = "SELECT * FROM TradingDeals";
        List<TradingDeal> deals = new ArrayList<>();
        try (PreparedStatement ps = unitOfWork.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TradingDeal deal = new TradingDeal();
                deal.setId(rs.getString("DealID"));
                deal.setUsername(rs.getString("Username"));
                deal.setCardToTrade(rs.getString("CardToTrade"));
                deal.setType(rs.getString("Type"));
                deal.setMinimumDamage(rs.getBigDecimal("MinimumDamage").doubleValue());
                deals.add(deal);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching trading deals.", e);
        }
        return deals;
    }

    public void executeTrade(UUID dealId, UUID offeredCardId, String tradingUser) throws DataAccessException {
        String sqlRemoveFromUserStacks = "DELETE FROM UserStacks WHERE Username = ? AND CardID = ?";
        String sqlAddToUserStacks = "INSERT INTO UserStacks (Username, CardID) VALUES (?, ?)";
        String sqlRemoveDeal = "DELETE FROM TradingDeals WHERE DealID = ?";

        try {
            TradingDeal deal = getDealById(dealId);
            if (deal == null) {
                throw new DataAccessException("Deal not found.");
            }

            String dealOwner = deal.getUsername();
            UUID cardToTradeId = UUID.fromString(deal.getCardToTrade());

            // delete offeredCardId from tradingUser's UserStacks
            try (PreparedStatement ps = unitOfWork.prepareStatement(sqlRemoveFromUserStacks)) {
                ps.setString(1, tradingUser);
                ps.setObject(2, offeredCardId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Offered card not found in user's stacks.");
                }
            }

            // insert offeredCardId to dealOwner's UserStacks
            try (PreparedStatement ps = unitOfWork.prepareStatement(sqlAddToUserStacks)) {
                ps.setString(1, dealOwner);
                ps.setObject(2, offeredCardId);
                ps.executeUpdate();
            }

            // delete cardToTradeId from dealOwner's UserStacks
            try (PreparedStatement ps = unitOfWork.prepareStatement(sqlRemoveFromUserStacks)) {
                ps.setString(1, dealOwner);
                ps.setObject(2, cardToTradeId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Card to trade not found in deal owner's stacks.");
                }
            }

            // insert cardToTradeId to tradingUser's UserStacks
            try (PreparedStatement ps = unitOfWork.prepareStatement(sqlAddToUserStacks)) {
                ps.setString(1, tradingUser);
                ps.setObject(2, cardToTradeId);
                ps.executeUpdate();
            }

            // delete trade
            try (PreparedStatement ps = unitOfWork.prepareStatement(sqlRemoveDeal)) {
                ps.setObject(1, dealId);
                ps.executeUpdate();
            }

            unitOfWork.commitTransaction();

        } catch (SQLException e) {
            unitOfWork.rollbackTransaction(); // Rollback when DataAccessException
            e.printStackTrace();
            throw new DataAccessException("Error executing trade: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            unitOfWork.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
}
