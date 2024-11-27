package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import game.Game;

public class GameToRestGameDtoConversor {

    public static List<RestGameDto> toRestGameDtos(List<Game> games) {
        List<RestGameDto> gameDtos = new ArrayList<>(games.size());
        for(int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            gameDtos.add(toRestGameDto(game));
        }
        return gameDtos;
    }

    public static RestGameDto toRestGameDto(Game game) {
        return new RestGameDto(game.getGameId(), game.getVisitantName(), game.getCelebrationDate().toString(), game.getPriceGame(),
                game.getTicketMaxCount(), game.getSoldTickets());
    }

    public static Game toGame(RestGameDto game) {
        return new Game(game.getGameId(), game.getVisitantName(), LocalDateTime.parse(game.getCelebrationDate()), game.getPriceGame(),
                game.getTicketMaxCount(), game.getSoldTickets());
    }
}

