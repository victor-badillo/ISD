package purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlPurchaseDao extends AbstractSqlPurchaseDao{
    @Override
    public Purchase create(Connection connection, Purchase purchase) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Purchase"
                + " (gameId, userEmail, creditCardNumber,"
                + " units, purchaseDate, pickedUp) VALUES (?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, purchase.getGameId());
            preparedStatement.setString(i++, purchase.getUserEmail());
            preparedStatement.setString(i++, purchase.getCreditCardNumber());
            preparedStatement.setInt(i++, purchase.getUnits());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(purchase.getPurchaseDate()));
            preparedStatement.setBoolean(i++, purchase.getPickedUp());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long purchaseId = resultSet.getLong(1);

            /* Return sale. */
            return new Purchase(purchaseId, purchase.getGameId(), purchase.getUserEmail(),
                    purchase.getCreditCardNumber(), purchase.getUnits(),
                    purchase.getPurchaseDate(), purchase.getPickedUp());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
