package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.app.client.service.dto.ClientGameDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientGameDtoConversor {
    public static ObjectNode toObjectNode(ClientGameDto game) throws IOException {

        ObjectNode gameObject = JsonNodeFactory.instance.objectNode();

        gameObject.put("gameId", game.getGameId()).
                put("visitantName", game.getVisitantName()).
                put("celebrationDate", game.getCelebrationDate()).
                put("priceGame", game.getPriceGame()).
                put("availableTickets", game.getAvailableTickets()).
                put("ticketMaxCount", game.getTicketMaxCount());

        return gameObject;
    }

    public static ClientGameDto toClientGameDto(InputStream jsonGame) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonGame);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientGameDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientGameDto> toClientGameDtos(InputStream jsonGames) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonGames);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode gamesArray = (ArrayNode) rootNode;
                List<ClientGameDto> gameDtos = new ArrayList<>(gamesArray.size());
                for (JsonNode gameNode : gamesArray) {
                    gameDtos.add(toClientGameDto(gameNode));
                }
                return gameDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientGameDto toClientGameDto(JsonNode gameNode) throws ParsingException {
        if (gameNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode gameObject = (ObjectNode) gameNode;

            JsonNode gameIdNode = gameObject.get("gameId");
            Long gameId = (gameIdNode != null) ? gameIdNode.longValue() : null;

            String visitantName = gameObject.get("visitantName").textValue().trim();
            String celebrationDate = gameObject.get("celebrationDate").textValue().trim();
            double priceGame = gameObject.get("priceGame").doubleValue();
            int soldTickets = gameObject.get("soldTickets").intValue();
            int ticketMaxCount = gameObject.get("ticketMaxCount").intValue();

            return new ClientGameDto(gameId, visitantName, celebrationDate, priceGame, ticketMaxCount-soldTickets, ticketMaxCount);
        }
    }
}