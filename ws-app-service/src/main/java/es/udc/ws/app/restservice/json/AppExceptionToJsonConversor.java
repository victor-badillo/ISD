package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;

public class AppExceptionToJsonConversor {
    public static ObjectNode toGameAlreadyPlayedException(GameAlreadyPlayedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "GameAlreadyPlayed");
        exceptionObject.put("gameId", (ex.getGameId() != null) ? ex.getGameId() : null);
        if (ex.getCelebrationDate() != null) {
            exceptionObject.put("celebrationDate", ex.getCelebrationDate().toString());
        } else {
            exceptionObject.set("celebrationDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toNotEnoughUnitsException(NotEnoughUnitsException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NotEnoughUnits");
        exceptionObject.put("gameId", (ex.getGameId() != null) ? ex.getGameId() : null);
        exceptionObject.put("units", ex.getUnits());
        exceptionObject.put("soldTickets", ex.getSoldTickets());

        return exceptionObject;
    }

    public static ObjectNode toNotValidCreditCardException(NotValidCreditCardException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NotValidCreditCard");
        if (ex.getCreditCardNumber() != null) {
            exceptionObject.put("creditCardNumber", ex.getCreditCardNumber());
        } else {
            exceptionObject.set("creditCardNumber", null);
        }
        exceptionObject.put("purchaseId", (ex.getPurchaseId() != null) ? ex.getPurchaseId() : null);

        return exceptionObject;
    }

    public static ObjectNode toTicketsAlreadyPicked(TicketsAlreadyPicked ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TicketsAlreadyPicked");
        exceptionObject.put("purchaseId", (ex.getPurchaseId() != null) ? ex.getPurchaseId() : null);

        return exceptionObject;
    }
}
