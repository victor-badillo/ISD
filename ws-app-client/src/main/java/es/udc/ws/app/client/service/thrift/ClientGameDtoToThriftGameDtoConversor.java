package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientGameDto;
import es.udc.ws.app.thrift.ThriftGameDto;
import java.util.ArrayList;
import java.util.List;

public class ClientGameDtoToThriftGameDtoConversor {

    public static ThriftGameDto toThriftGameDto(
            ClientGameDto clientGameDto) {

        Long gameId = clientGameDto.getGameId();

        return new ThriftGameDto(
                gameId == null ? -1 : gameId.longValue(),
                clientGameDto.getVisitantName(),
                clientGameDto.getCelebrationDate(),
                clientGameDto.getPriceGame(),
                clientGameDto.getTicketMaxCount(),
                0);

    }

    public static List<ClientGameDto> toClientGameDtos(List<ThriftGameDto> games) {

        List<ClientGameDto> clientGameDtos = new ArrayList<>(games.size());

        for (ThriftGameDto game : games) {
            clientGameDtos.add(toClientGameDto(game));
        }
        return clientGameDtos;

    }

    public static ClientGameDto toClientGameDto(ThriftGameDto game) {

        return new ClientGameDto(
                game.getGameId(),
                game.getVisitantName(),
                game.getCelebrationDate(),
                game.getPriceGame(),
                game.getTicketMaxCount()- game.getSoldTickets(),
                game.getTicketMaxCount());

    }

}
