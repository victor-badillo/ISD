package purchase;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import game.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlPurchaseDao implements SqlPurchaseDao{
    protected AbstractSqlPurchaseDao() {
    }

    @Override
    public List<Purchase> findUserPurchases(Connection connection, String userEmail) {
        /* Create "queryString". */
        String queryString = "SELECT gameId, purchaseId, creditCardNumber,  "
                + "units, purchaseDate, pickedUp FROM Purchase WHERE userEmail = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, userEmail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Purchase> purchases = new ArrayList<Purchase>();

            while (resultSet.next()) {
                i = 1;
                Long gameId = resultSet.getLong(i++);
                Long purchaseId = resultSet.getLong(i++);
                String creditCardNumber = resultSet.getString(i++);
                int units = resultSet.getInt(i++);
                Timestamp purchaseDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime purchaseDate = purchaseDateAsTimestamp.toLocalDateTime();
                boolean pickedUp = resultSet.getBoolean(i++);

                purchases.add(new Purchase(purchaseId, gameId, userEmail, creditCardNumber, units, purchaseDate, pickedUp));
            }
            /* Return List of purchases. */
            return purchases;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Purchase find(Connection connection, Long purchaseId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "SELECT gameId, userEmail, creditCardNumber, "
                + " units, purchaseDate, pickedUp FROM Purchase WHERE purchaseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, purchaseId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(purchaseId,
                        Game.class.getName());
            }

            /* Get results. */
            i = 1;
            Long gameId = resultSet.getLong(i++);
            String userEmail = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            int units = resultSet.getInt(i++);
            Timestamp purchaseDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime purchaseDate = purchaseDateAsTimestamp.toLocalDateTime();
            boolean pickedUp = resultSet.getBoolean(i++);

            /* Return game. */
            return new Purchase(purchaseId, gameId, userEmail, creditCardNumber, units, purchaseDate, pickedUp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePurchase(Connection connection, Purchase purchase) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "UPDATE Purchase SET gameId = ?, userEmail = ?, creditCardNumber = ?, units = ?, pickedUp = ? WHERE purchaseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, purchase.getGameId());
            preparedStatement.setString(i++, purchase.getUserEmail());
            preparedStatement.setString(i++, purchase.getCreditCardNumber());
            preparedStatement.setFloat(i++, purchase.getUnits());
            preparedStatement.setBoolean(i++, purchase.getPickedUp());
            preparedStatement.setLong(i++, purchase.getPurchaseId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(purchase.getPurchaseId(),
                        Purchase.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long purchaseId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Purchase WHERE" + " purchaseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, purchaseId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(purchaseId,
                        Game.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}