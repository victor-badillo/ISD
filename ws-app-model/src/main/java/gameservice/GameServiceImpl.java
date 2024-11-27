package gameservice;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import game.Game;
import game.SqlGameDao;
import game.SqlGameDaoFactory;

import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;

import purchase.Purchase;
import purchase.SqlPurchaseDao;
import purchase.SqlPurchaseDaoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;


public class GameServiceImpl implements GameService{
    private final DataSource dataSource;
    private SqlGameDao gameDao = null;

    private SqlPurchaseDao purchaseDao = null;

    public GameServiceImpl(){
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        gameDao = SqlGameDaoFactory.getDao();
        purchaseDao = SqlPurchaseDaoFactory.getDao();
    }

    private static void validateInt(String propertyName, int value, int lowerValidLimit, int upperValidLimit) throws InputValidationException {
        if ( (value < lowerValidLimit) || (value > upperValidLimit) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than " + lowerValidLimit +
                    " and lower than " + upperValidLimit + "): " + value);
        }
    }
    private static void validateCelebrationDate(String propertyName, LocalDateTime celebrationDate ) throws InputValidationException {
        LocalDateTime actualDay = LocalDateTime.now().withNano(0);

        if(celebrationDate == null || celebrationDate.isBefore(actualDay)){
            throw new InputValidationException("Invalid " + propertyName + ", must be after the actual date");
        }
    }
    private void validateGame(Game game) throws InputValidationException {
        PropertyValidator.validateMandatoryString("visitantName", game.getVisitantName());
        PropertyValidator.validateDouble("priceGame", game.getPriceGame(), 1, 1000000);
        validateInt("ticketMaxCount", game.getTicketMaxCount(), 1, 200005);
        validateCelebrationDate("celebrationDate", game.getCelebrationDate());


    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) throws InputValidationException{
        LocalDateTime actualDay = LocalDateTime.now().withNano(0);
        if(startDate == null || endDate == null){
            throw new InputValidationException("Invalid input dates, cannot be null");
        }
        if(startDate.isBefore(actualDay)){
            throw new InputValidationException("Invalid input dates, starting date cannot be before the actual date");
        }

        if(endDate.isBefore(startDate)){
            throw new InputValidationException("Invalid input dates, end date cannot be before the start date");
        }
    }

    private void validateUserEmailFormat(String userEmail) throws InputValidationException{
        // Patrón de expresión regular para validar el formato de correo electrónico
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)$";
        Pattern pattern = Pattern.compile(emailPattern);  //expresion en un patron
        Matcher matcher = pattern.matcher(userEmail);     //para comparar el patron y el email

        if(!matcher.matches()){
            throw new InputValidationException("Invalid email, it must follow the format: user@domain.*");
        }
    }

    private void validateUnits(int units) throws InputValidationException{
        if(units <= 0){
            throw new InputValidationException("Invalid number of units, it cannot be less or equal than 0");
        }
    }


    @Override
    public Game addGame(Game game) throws InputValidationException {
        validateGame(game);
        game.setCreationDate(LocalDateTime.now().withNano(0));
        game.setSoldTickets(0);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Game createdGame = gameDao.create(connection, game);

                /* Commit. */
                connection.commit();

                return createdGame;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> findGameByDate(LocalDateTime startDate, LocalDateTime endDate) throws InputValidationException{
        validateDates(startDate, endDate);
        try (Connection connection = dataSource.getConnection()) {
            return gameDao.findGamesByDate(connection, startDate, endDate);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game findGameById(Long gameId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return gameDao.find(connection, gameId);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Purchase purchaseGame(String userEmail, Long gameId, String creditCardNumber, int units)
            throws InputValidationException, InstanceNotFoundException, NotEnoughUnitsException, GameAlreadyPlayedException {
        PropertyValidator.validateMandatoryString("userEmail",userEmail);
        validateUserEmailFormat(userEmail);
        PropertyValidator.validateCreditCard(creditCardNumber);
        validateUnits(units);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Game game = gameDao.find(connection, gameId);

                //Check if the game was already played
                if(LocalDateTime.now().withNano(0).isAfter(game.getCelebrationDate())){
                    throw new GameAlreadyPlayedException(gameId, game.getCelebrationDate());
                }

                //Check if there are enough tickets for purchase
                if(units > (game.getTicketMaxCount() - game.getSoldTickets())){
                    throw new NotEnoughUnitsException(gameId, units, game.getSoldTickets());

                }

                /* Do work. */
                Purchase purchase = purchaseDao.create(connection, new Purchase(gameId, userEmail, creditCardNumber,
                        units, LocalDateTime.now().withNano(0), false));
                //Update the number of sold tickets
                game.setSoldTickets(game.getSoldTickets() + purchase.getUnits());
                gameDao.updateGame(connection,game);
                /* Commit. */
                connection.commit();

                return purchase;

            } catch (InstanceNotFoundException | GameAlreadyPlayedException | NotEnoughUnitsException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Purchase> findUserPurchases(String userEmail) throws InputValidationException {
        PropertyValidator.validateMandatoryString("userEmail",userEmail);
        validateUserEmailFormat(userEmail);
        try (Connection connection = dataSource.getConnection()) {
            return purchaseDao.findUserPurchases(connection, userEmail);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickedTickets(Long purchaseId, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, NotValidCreditCardException, TicketsAlreadyPicked {
        PropertyValidator.validateCreditCard(creditCardNumber);
        try (Connection connection = dataSource.getConnection()) {
            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Purchase purchase = purchaseDao.find(connection, purchaseId);
                //check if the cardnumber is the same as the user
                if(!creditCardNumber.equals(purchase.getCreditCardNumber()))
                    throw new NotValidCreditCardException(creditCardNumber, purchase.getPurchaseId());
                if(purchase.getPickedUp())
                    throw new TicketsAlreadyPicked(purchaseId);
                purchase.setPickedUp(true);
                purchaseDao.updatePurchase(connection, purchase);
                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException | NotValidCreditCardException | TicketsAlreadyPicked e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}