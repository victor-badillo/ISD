package game;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractSqlGameDao implements SqlGameDao{
    protected AbstractSqlGameDao() {
    }

    @Override
    public Game find(Connection connection, Long gameId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT visitantName, celebrationDate, "
                + " priceGame, ticketMaxCount, creationDate, soldTickets FROM Game WHERE gameId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, gameId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(gameId,
                        Game.class.getName());
            }

            /* Get results. */
            i = 1;
            String visitantName = resultSet.getString(i++);
            Timestamp celebrationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime celebrationDate = celebrationDateAsTimestamp.toLocalDateTime();
            double priceGame = resultSet.getDouble(i++);
            int ticketMaxCount = resultSet.getInt(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
            int soldTickets = resultSet.getInt(i++);

            /* Return game. */
            return new Game(gameId, visitantName, celebrationDate, priceGame,ticketMaxCount,creationDate,
                    soldTickets);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> findGamesByDate(Connection connection, LocalDateTime startDate, LocalDateTime endDate) {
        String queryString = "SELECT gameId, visitantName, celebrationDate, priceGame, ticketMaxCount, creationDate, soldTickets "
                + "FROM Game WHERE celebrationDate >= ? AND celebrationDate <= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Game> games = new ArrayList<>();

            while (resultSet.next()) {
                i=1;
                long gameId = Long.valueOf(resultSet.getLong(i++));
                String visitantName = resultSet.getString(i++);
                Timestamp celebrationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime celebrationDate = celebrationDateAsTimestamp.toLocalDateTime();
                double priceGame = resultSet.getDouble(i++);
                int ticketMaxCount = resultSet.getInt(i++);
                Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
                int soldTickets = resultSet.getInt(i++);

                games.add(new Game(gameId, visitantName, celebrationDate, priceGame, ticketMaxCount, creationDate, soldTickets));
            }

            return games;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(Connection connection, Game game) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Game"
                + " SET visitantName = ?, celebrationDate = ?, priceGame = ?, "
                + "ticketMaxCount = ?, soldTickets = ? WHERE gameId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, game.getVisitantName());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(game.getCelebrationDate()));
            preparedStatement.setDouble(i++, game.getPriceGame());
            preparedStatement.setInt(i++, game.getTicketMaxCount());
            preparedStatement.setInt(i++, game.getSoldTickets());
            preparedStatement.setLong(i++, game.getGameId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(game.getGameId(),
                        Game.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long gameId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Game WHERE" + " gameId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, gameId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(gameId,
                        Game.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
