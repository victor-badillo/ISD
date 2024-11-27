package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientGameAlreadyPlayedException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientNotValidCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyPickedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.client.service.dto.ClientGameDto;
import java.util.List;

public interface ClientGameService {

    public ClientGameDto addGame(ClientGameDto gameDto)
            throws InputValidationException;

    public List<ClientGameDto> findGameByDate(String specifiedDate)
            throws InputValidationException;

    public ClientGameDto findGameById(Long gameId)
            throws InstanceNotFoundException;

    public ClientPurchaseDto purchaseGame(String userEmail, Long gameId, String creditCardNumber, int units)
            throws InputValidationException, InstanceNotFoundException,
            ClientNotEnoughUnitsException, ClientGameAlreadyPlayedException;

    public List<ClientPurchaseDto> findUserPurchases(String userEmail)
            throws InputValidationException;

    public void pickedTickets(Long purchaseId, String creditCardNumber)
            throws InputValidationException, InstanceNotFoundException,
            ClientNotValidCreditCardException, ClientTicketsAlreadyPickedException;
}
