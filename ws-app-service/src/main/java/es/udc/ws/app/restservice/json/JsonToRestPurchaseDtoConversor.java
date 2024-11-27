package es.udc.ws.app.restservice.json;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestPurchaseDto;
import java.util.List;

public class JsonToRestPurchaseDtoConversor {

    public static ObjectNode toObjectNode(RestPurchaseDto purchase) {

        ObjectNode purchaseObject = JsonNodeFactory.instance.objectNode();

        purchaseObject.put("gameId", purchase.getGameId()).
                put("purchaseId", purchase.getPurchaseId()).
                put("userEmail", purchase.getUserEmail()).
                put("lastFourDigits", purchase.getLastFourDigits()).
                put("units", purchase.getUnits()).
                put("purchaseDate", purchase.getPurchaseDate()).
                put("pickedUp", purchase.getPickedUp());

        return purchaseObject;
    }

    public static ArrayNode toArrayNode(List<RestPurchaseDto> purchases) {

        ArrayNode purchasesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < purchases.size(); i++) {
            RestPurchaseDto purchaseDto = purchases.get(i);
            ObjectNode purchaseObject = toObjectNode(purchaseDto);
            purchasesNode.add(purchaseObject);
        }

        return purchasesNode;
    }

}