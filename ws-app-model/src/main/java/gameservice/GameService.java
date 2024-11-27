package gameservice;


import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import game.Game;

import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;

import purchase.Purchase;

import java.time.LocalDateTime;
import java.util.List;

public interface GameService {
    //Validaciones InputValidationException:
    //  -game.celebrationDate < dia actual || game.celebrationDate == null
    //  -game.priceGame <= 0
    //  -game.ticketMaxCount <=0
    //  -game.visitantName == null || game.visitantName.length() == 0
    public Game addGame(Game game) throws InputValidationException;


    //Informacion o partidos que estan por disputarse
    //Validaciones InputValidationException:
    //  -startDate <= día actual
    //  -endDate < startDate
    //  -(endDate || startDate) == null
    public List<Game> findGameByDate (LocalDateTime startDate, LocalDateTime endDate) throws InputValidationException;

    public Game findGameById(Long gameId) throws InstanceNotFoundException;


    //Validaciones InputValidationException:
    //  -email == null || email no sigue el patron user@domain
    //  -units <= 0
    //  -creditNumber: no formado por 16 dígitos numéricos
    //  -email.length() == 0
    //Validaciones InstanceNotFoundException
    //  -gameId no existe
    //Validaciones NotEnoughUnitsException
    //  -units > dbGame.ticketMaxCount - dbGame.soldTickets
    //Validaciones GameAlreadyPlayedException
    //  -LocalDateTime.now > dbGame.celebrationDate
    public Purchase purchaseGame(String userEmail, Long gameId, String creditCardNumber, int units) throws
            InputValidationException, InstanceNotFoundException, NotEnoughUnitsException, GameAlreadyPlayedException;


    //Validaciones InputValidationException:
    //  -email == null || email no sigue el patron user@domain  ||  email.length() == 0
    public List<Purchase> findUserPurchases(String userEmail) throws InputValidationException;


    //Validaciones InputValidationException:
    //  -creditCardNumber == null
    //  -creditCardNumber: no formado por 16 dígitos numéricos
    //Validaciones NotValidCreditcardException
    //  -creditCardNumber != dbPurchase.creditCardNumber
    //Validaciones TicketsAlreadyPicked:
    //  -pickedUp == true
    public void pickedTickets(Long purchaseId, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, NotValidCreditCardException, TicketsAlreadyPicked;
}