package game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Jdbc3CcSqlGameDao extends AbstractSqlGameDao{
    @Override
    public Game create(Connection connection, Game game) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Game"
                + " (visitantName, celebrationDate, priceGame, ticketMaxCount, creationDate, soldTickets)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, game.getVisitantName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(game.getCelebrationDate()));
            preparedStatement.setDouble(i++, game.getPriceGame());
            preparedStatement.setInt(i++, game.getTicketMaxCount());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(game.getCreationDate()));
            preparedStatement.setInt(i++, game.getSoldTickets());


            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long gameId = resultSet.getLong(1);

            /* Return movie. */
            return new Game(gameId, game.getVisitantName(), game.getCelebrationDate(),
                    game.getPriceGame(), game.getTicketMaxCount(),
                    game.getCreationDate(), game.getSoldTickets());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
