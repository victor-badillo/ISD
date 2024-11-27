package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import game.Game;
import gameservice.GameServiceFactory;
import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;
import purchase.Purchase;
import java.time.LocalDateTime;
import java.util.List;

public class ThriftGameServiceImpl implements ThriftGameService.Iface {
    @Override
    public ThriftGameDto addGame(ThriftGameDto gameDto) throws ThriftInputValidationException{
        Game game = GameToThriftGameDtoConversor.toGame(gameDto);

        try {
            Game addedGame = GameServiceFactory.getService().addGame(game);
            return GameToThriftGameDtoConversor.toThriftGameDto(addedGame);
        }catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftGameDto> findGameByDate(String specifiedDate) throws ThriftInputValidationException{
        try {
            List<Game> games = GameServiceFactory.getService().findGameByDate(LocalDateTime.now().withNano(0), LocalDateTime.parse(specifiedDate));
            return GameToThriftGameDtoConversor.toThriftGameDtos(games);

        }catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public ThriftGameDto findGameById(long gameId) throws ThriftInstanceNotFoundException{
        try {
            Game game = GameServiceFactory.getService().findGameById(gameId);
            return GameToThriftGameDtoConversor.toThriftGameDto(game);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }

    @Override
    public ThriftPurchaseDto purchaseGame(String userEmail, long gameId, String creditCardNumber, int units) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftGameAlreadyPlayedException, ThriftNotEnoughUnitsException{
        try {

            Purchase purchase = GameServiceFactory.getService().purchaseGame(userEmail, gameId,creditCardNumber,units);
            return PurchaseToThriftPurchaseDtoConversor.toThriftPurchaseDto(purchase);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (GameAlreadyPlayedException e){
            throw new ThriftGameAlreadyPlayedException(e.getGameId(), e.getCelebrationDate().toString());
        } catch (NotEnoughUnitsException e){
            throw new ThriftNotEnoughUnitsException(e.getGameId(), e.getSoldTickets(), e.getUnits());

        }
    }

    @Override
    public List<ThriftPurchaseDto> findUserPurchases(String userEmail) throws ThriftInputValidationException {
        try {
            List<Purchase> userpurchases = GameServiceFactory.getService().findUserPurchases(userEmail);
            return PurchaseToThriftPurchaseDtoConversor.toThriftPurchaseDtos(userpurchases);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void pickedTickets(long purchaseId, String creditCardNumber) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftNotValidCreditCardException, ThriftTicketsAlreadyPickedException {
        try{
            GameServiceFactory.getService().pickedTickets(purchaseId, creditCardNumber);
        }catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }catch (NotValidCreditCardException e){
            throw new ThriftNotValidCreditCardException(e.getCreditCardNumber(),e.getPurchaseId());
        }catch (TicketsAlreadyPicked e){
            throw new ThriftTicketsAlreadyPickedException(e.getPurchaseId());
        }
    }
}
