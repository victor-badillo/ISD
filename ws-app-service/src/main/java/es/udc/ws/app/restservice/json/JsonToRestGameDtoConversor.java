package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestGameDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestGameDtoConversor {

    public static ObjectNode toObjectNode(RestGameDto game) {

        ObjectNode gameObject = JsonNodeFactory.instance.objectNode();

        gameObject.put("gameId", game.getGameId()).
                put("visitantName", game.getVisitantName()).
                put("celebrationDate", game.getCelebrationDate()).
                put("priceGame", game.getPriceGame()).
                put("ticketMaxCount", game.getTicketMaxCount()).
                put("soldTickets", game.getSoldTickets());

        return gameObject;
    }

    public static ArrayNode toArrayNode(List<RestGameDto> games) {

        ArrayNode gamesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < games.size(); i++) {
            RestGameDto gameDto = games.get(i);
            ObjectNode gameObject = toObjectNode(gameDto);
            gamesNode.add(gameObject);
        }

        return gamesNode;
    }

    public static RestGameDto toRestGameDto(InputStream jsonGame) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonGame);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode gameObject = (ObjectNode) rootNode;

                JsonNode gameIdNode = gameObject.get("gameId");
                Long gameId = (gameIdNode != null) ? gameIdNode.longValue() : null;

                String visitantName = gameObject.get("visitantName").textValue().trim();
                String celebrationDate = gameObject.get("celebrationDate").textValue().trim();
                double priceGame =  gameObject.get("priceGame").doubleValue();
                int ticketMaxCount = gameObject.get("ticketMaxCount").intValue();

                return new RestGameDto(gameId, visitantName, celebrationDate, priceGame, ticketMaxCount,0);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
