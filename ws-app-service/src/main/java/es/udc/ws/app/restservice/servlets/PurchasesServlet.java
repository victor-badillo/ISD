package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.restservice.dto.PurchaseToRestPurchaseDtoConversor;
import es.udc.ws.app.restservice.dto.RestPurchaseDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestPurchaseDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import gameservice.GameServiceFactory;
import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import purchase.Purchase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PurchasesServlet extends RestHttpServletTemplate {


    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){

            String userEmail = ServletUtils.getMandatoryParameter(req,"userEmail");
            Long gameId = ServletUtils.getMandatoryParameterAsLong(req,"gameId");
            String creditCardNumber = ServletUtils.getMandatoryParameter(req,"creditCardNumber");
            int units = Integer.parseInt(ServletUtils.getMandatoryParameter(req,"units"));

            Purchase purchase;
            try {
                purchase = GameServiceFactory.getService().purchaseGame(userEmail, gameId, creditCardNumber, units);
            } catch (NotEnoughUnitsException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toNotEnoughUnitsException(ex),
                        null);
                return;
            } catch (GameAlreadyPlayedException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toGameAlreadyPlayedException(ex),
                        null);
                return;
            }

            RestPurchaseDto purchaseDto = PurchaseToRestPurchaseDtoConversor.toRestPurchaseDto(purchase);
            String purchaseURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + purchase.getGameId().toString();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", purchaseURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestPurchaseDtoConversor.toObjectNode(purchaseDto), headers);

        } else{ //PickedTickets overloaded post

            String creditCardNumber = ServletUtils.getMandatoryParameter(req,"creditCardNumber");
            String[] url = req.getPathInfo().split("/");
            if(url.length != 3 || Long.parseLong(url[1]) < 1 || !(url[2].equals("pickedTickets")) ){
                throw new InputValidationException("Invalid Request: " + "invalid path " + req.getPathInfo());
            }
            Long purchaseId = Long.parseLong(url[1]);

            try {
                GameServiceFactory.getService().pickedTickets(purchaseId, creditCardNumber);

            } catch (NotValidCreditCardException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toNotValidCreditCardException(ex),
                        null);
                return;
            } catch (TicketsAlreadyPicked ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toTicketsAlreadyPicked(ex),
                        null);
                return;
            }
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT,null , null);

        }

    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String userEmail = req.getParameter("userEmail");

        List<Purchase> purchases = GameServiceFactory.getService().findUserPurchases(userEmail);

        List<RestPurchaseDto> purchaseDtos = PurchaseToRestPurchaseDtoConversor.toRestPurchaseDtos(purchases);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestPurchaseDtoConversor.toArrayNode(purchaseDtos), null);
    }
}