package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.app.client.service.exceptions.ClientGameAlreadyPlayedException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientNotValidCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyPickedException;
import java.io.InputStream;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if(errorType.equals("NotEnoughUnits")){
                    return toNotEnoughUnitsException(rootNode);
                } else if(errorType.equals("GameAlreadyPlayed")){
                    return toGameAlreadyPlayedException(rootNode);
                }else if (errorType.equals("TicketsAlreadyPicked")) {
                    return toTicketsAlreadyPickedException(rootNode);
                }else if (errorType.equals("NotValidCreditCard")) {
                    return toNotValidCreditCardException(rootNode);
                }else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    private static ClientTicketsAlreadyPickedException toTicketsAlreadyPickedException(JsonNode rootNode) {
        Long purchaseId = rootNode.get("purchaseId").longValue();
        return new ClientTicketsAlreadyPickedException(purchaseId);
    }

    private static ClientNotEnoughUnitsException toNotEnoughUnitsException(JsonNode rootNode) {
        Long gameId = rootNode.get("gameId").longValue();
        int units = rootNode.get("units").intValue();
        int soldTickets = rootNode.get("soldTickets").intValue();
        return new ClientNotEnoughUnitsException(gameId, units, soldTickets);
    }

    private static ClientGameAlreadyPlayedException toGameAlreadyPlayedException(JsonNode rootNode) {
        Long gameId = rootNode.get("gameId").longValue();
        String celebrationDate= rootNode.get("celebrationDate").textValue();
        return new ClientGameAlreadyPlayedException(gameId, celebrationDate);
    }
    private static ClientNotValidCreditCardException toNotValidCreditCardException(JsonNode rootNode) {
        Long purchaseId = rootNode.get("purchaseId").longValue();
        String creditCardNumber = rootNode.get("creditCardNumber").textValue();
        return new ClientNotValidCreditCardException(creditCardNumber, purchaseId);
    }


}