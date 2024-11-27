package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftGameDto;
import game.Game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameToThriftGameDtoConversor {

    public static Game toGame(ThriftGameDto game) {
        return new Game(game.getGameId(), game.getVisitantName(), LocalDateTime.parse(game.getCelebrationDate()),
                game.getPriceGame(), game.getTicketMaxCount(), game.getSoldTickets());
    }

    public static List<ThriftGameDto> toThriftGameDtos(List<Game> games) {

        List<ThriftGameDto> dtos = new ArrayList<>(games.size());

        for (Game game : games) {
            dtos.add(toThriftGameDto(game));
        }
        return dtos;

    }

    public static ThriftGameDto toThriftGameDto(Game game) {

        return new ThriftGameDto(game.getGameId(), game.getVisitantName(),
                game.getCelebrationDate().toString(), game.getPriceGame(),
                game.getTicketMaxCount(), game.getSoldTickets());

    }

}
