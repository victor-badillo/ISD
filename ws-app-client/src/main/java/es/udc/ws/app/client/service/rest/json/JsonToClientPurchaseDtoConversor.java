package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientPurchaseDtoConversor {

    public static ClientPurchaseDto toClientPurchaseDto(InputStream jsonPurchase) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPurchase);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientPurchaseDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientPurchaseDto> toClientPurchaseDtos(InputStream jsonPurchases) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPurchases);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode purchasesArray = (ArrayNode) rootNode;
                List<ClientPurchaseDto> purchaseDtos = new ArrayList<>(purchasesArray.size());
                for (JsonNode purchaseNode : purchasesArray) {
                    purchaseDtos.add(toClientPurchaseDto(purchaseNode));
                }
                return purchaseDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientPurchaseDto toClientPurchaseDto(JsonNode purchaseNode) throws ParsingException {
        if (purchaseNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode purchaseObject = (ObjectNode) purchaseNode;

            JsonNode purchaseIdNode = purchaseObject.get("purchaseId");
            Long purchaseId = (purchaseIdNode != null) ? purchaseIdNode.longValue() : null;

            Long gameId = purchaseObject.get("gameId").longValue();
            String userEmail = purchaseObject.get("userEmail").textValue().trim();
            String lastFourDigits = purchaseObject.get("lastFourDigits").textValue().trim();
            int units = purchaseObject.get("units").intValue();
            String purchaseDate = purchaseObject.get("purchaseDate").textValue().trim();
            Boolean pickedUp = purchaseObject.get("pickedUp").booleanValue();

            return new ClientPurchaseDto(gameId,purchaseId, userEmail, lastFourDigits, units, purchaseDate, pickedUp);
        }
    }
}