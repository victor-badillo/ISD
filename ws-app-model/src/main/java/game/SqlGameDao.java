package game;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlGameDao {
    public Game create(Connection connection, Game game);

    public Game find(Connection connection, Long gameId) throws InstanceNotFoundException;

    public List<Game> findGamesByDate(Connection connection, LocalDateTime startDate, LocalDateTime endDate);

    public void updateGame(Connection connection, Game game) throws InstanceNotFoundException;

    // Necesario para las pruebas
    public void remove(Connection connection, Long gameId) throws InstanceNotFoundException;
}