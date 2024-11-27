package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.util.sql.SimpleDataSource;
import es.udc.ws.util.sql.DataSourceLocator;

import game.Game;
import game.SqlGameDao;
import game.SqlGameDaoFactory;

import gameservice.GameService;
import gameservice.GameServiceFactory;

import gameservice.exceptions.GameAlreadyPlayedException;
import gameservice.exceptions.NotEnoughUnitsException;
import gameservice.exceptions.NotValidCreditCardException;
import gameservice.exceptions.TicketsAlreadyPicked;

import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import purchase.Purchase;
import purchase.SqlPurchaseDao;
import purchase.SqlPurchaseDaoFactory;


public class AppServiceTest {
    private final long NON_EXISTENT_GAME_ID = -1;
    private final String USER_EMAIL = "legide@cortinas.tuki";
    private final int UNITS = 300;
    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private static GameService gameService = null;
    private static SqlGameDao gameDao = null;
    private static SqlPurchaseDao purchaseDao = null;
    private final String INVALID_CREDIT_CARD_NUMBER = "";


    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        gameService = GameServiceFactory.getService();
        gameDao = SqlGameDaoFactory.getDao();
        purchaseDao = SqlPurchaseDaoFactory.getDao();

    }

    private Game createGame(Game game) {
        Game addedGame = null;
        try {
            addedGame = gameService.addGame(game);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedGame;

    }
    private Game getValidGame(String visitantName, LocalDateTime celebrationDate) {
        return new Game(visitantName,celebrationDate,120.50, 10000);
    }

    private Game getValidGame() {
        LocalDateTime celebrationDate = LocalDateTime.now().plusDays(10).withNano(0);
        return getValidGame("Los castaÃ±as",celebrationDate );
    }

    private void removeGame(Long gameId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                gameDao.remove(connection, gameId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void updateGame(Game game){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                gameDao.updateGame(connection, game);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removePurchase(Long purchaseId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                purchaseDao.remove(connection, purchaseId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Purchase findPurchase(Long purchaseId){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {
                return purchaseDao.find(connection, purchaseId);
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePurchase(Purchase purchase){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                purchaseDao.updatePurchase(connection, purchase);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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


    @Test
    public void testAddGameAndFindGame() throws InputValidationException, InstanceNotFoundException {
        Game game = getValidGame();
        Game addedGame = null;

        try {

            // Create Game
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedGame = gameService.addGame(game);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find Game
            Game foundGame = gameService.findGameById(addedGame.getGameId());

            assertEquals(addedGame, foundGame);
            assertEquals(foundGame.getVisitantName(),game.getVisitantName());
            assertEquals(foundGame.getCelebrationDate(),game.getCelebrationDate());
            assertEquals(foundGame.getPriceGame(),game.getPriceGame());
            assertEquals(foundGame.getTicketMaxCount(),game.getTicketMaxCount());
            assertTrue((foundGame.getCreationDate().compareTo(beforeCreationDate) >= 0)
                    && (foundGame.getCreationDate().compareTo(afterCreationDate) <= 0));
            assertEquals(foundGame.getSoldTickets(), game.getSoldTickets());

        } finally {
            // Clear Database
            if (addedGame!=null) {
                removeGame(addedGame.getGameId());
            }
        }
    }

    @Test
    public void testAddInvalidGame() {
        // Check visitantName not null
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setVisitantName(null);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check visitantName not empty
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setVisitantName("");
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check celebrationDate not null
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setCelebrationDate(null);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check celebrationDate is not before actual date
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            LocalDateTime beforeActualDate = LocalDateTime.of(2023,10,23,17,10).withNano(0);
            game.setCelebrationDate(beforeActualDate);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check priceGame > 0
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setPriceGame(-1);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check ticketMaxCount > 0
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setTicketMaxCount(-1);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });

        // Check ticketMaxCount > 0
        assertThrows(InputValidationException.class, () -> {
            Game game = getValidGame();
            game.setTicketMaxCount(-1);
            Game addedGame = gameService.addGame(game);
            removeGame(addedGame.getGameId());
        });
    }

    @Test
    public void testFindNonExistentGame() {
        assertThrows(InstanceNotFoundException.class, () -> gameService.findGameById(NON_EXISTENT_GAME_ID));
    }

    @Test
    public void testFindGamesByDate() throws InputValidationException{
        // Add games
        List<Game> games = new LinkedList<>();
        LocalDateTime date1 = LocalDateTime.of(2030,10,26,17,0,0).withNano(0);
        Game game1 = createGame(getValidGame("Los badillos",date1));
        games.add(game1);
        LocalDateTime date2 = LocalDateTime.of(2030,10,29,17,0,0).withNano(0);
        Game game2 = createGame(getValidGame("Los emilios",date2));
        games.add(game2);
        LocalDateTime date3 = LocalDateTime.of(2030,10,29,19,20,0).withNano(0);
        Game game3 = createGame(getValidGame("Los legis",date3));
        games.add(game3);

        try {
            LocalDateTime dateStart = LocalDateTime.of(2030,10,26,10,0,0).withNano(0);
            LocalDateTime dateEnd = LocalDateTime.of(2030,10,30,10,0,0).withNano(0);

            List<Game> foundGames = gameService.findGameByDate(dateStart, dateEnd);
            assertEquals(games, foundGames);

            foundGames = gameService.findGameByDate(LocalDateTime.of(2030,10,28,10,0,0).withNano(0), LocalDateTime.of(2030,10,29,18,0,0).withNano(0));
            assertEquals(1, foundGames.size());
            assertEquals(games.get(1), foundGames.get(0));

            foundGames = gameService.findGameByDate(LocalDateTime.of(2031,10,26,10,0,0).withNano(0), LocalDateTime.of(2032,10,26,10,0,0).withNano(0));
            assertEquals(0, foundGames.size());
        } finally {
            // Clear Database
            for (Game game : games) {
                removeGame(game.getGameId());
            }
        }

    }

    @Test
    public void testInvalidFindGamesByDate() {
        //Check startDate != null
        assertThrows(InputValidationException.class, () -> {
            LocalDateTime startDate = null;
            LocalDateTime endDate = LocalDateTime.of(2050,12,12,12,12,12).withNano(0);
            gameService.findGameByDate(startDate,endDate);
        });

        // Check endDate != null
        assertThrows(InputValidationException.class, () -> {
            LocalDateTime startDate = LocalDateTime.of(2049,12,12,12,12,12).withNano(0);
            LocalDateTime endDate = null;
            gameService.findGameByDate(startDate,endDate);
        });

        // Check startDate > actual day
        assertThrows(InputValidationException.class, () -> {
            LocalDateTime startDate = LocalDateTime.of(2000,12,12,12,12,12).withNano(0);
            LocalDateTime endDate = LocalDateTime.of(2050,12,12,12,12,12).withNano(0);
            gameService.findGameByDate(startDate,endDate);
        });

        //Check endDate > startDate
        assertThrows(InputValidationException.class, () -> {
            LocalDateTime startDate = LocalDateTime.of(2049,12,12,12,12,12).withNano(0);
            LocalDateTime endDate = LocalDateTime.of(2030,12,12,12,12,12).withNano(0);
            gameService.findGameByDate(startDate,endDate);
        });
    }


    @Test
    public void testBuyGame()
            throws InstanceNotFoundException, InputValidationException, NotEnoughUnitsException, GameAlreadyPlayedException {
        Game game = createGame(getValidGame());
        Purchase purchase = null;

        try {

            // Buy Game
            LocalDateTime beforeBuyDate = LocalDateTime.now().withNano(0);

            purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);

            LocalDateTime afterBuyDate = LocalDateTime.now().withNano(0);

            // Find sale
            Purchase foundPurchase = findPurchase(purchase.getPurchaseId());

            //Get game
            Game foundGame = gameService.findGameById(game.getGameId());

            // Check purchase
            assertEquals(purchase, foundPurchase);
            assertEquals(USER_EMAIL, foundPurchase.getUserEmail());
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundPurchase.getCreditCardNumber());
            assertEquals(UNITS, foundPurchase.getUnits());
            assertEquals(game.getGameId(), foundPurchase.getGameId());
            assertTrue((foundPurchase.getPurchaseDate().compareTo(beforeBuyDate) >= 0)
                    && (foundPurchase.getPurchaseDate().compareTo(afterBuyDate) <= 0));
            assertFalse(foundPurchase.getPickedUp());
            assertTrue(game.getSoldTickets() < foundGame.getSoldTickets());

        } finally {
            // Clear database: remove sale (if created) and game
            if (purchase != null) {
                removePurchase(purchase.getPurchaseId());
            }
            removeGame(game.getGameId());
        }

    }

    @Test
    public void testBuyNonExistentGame(){
        assertThrows(InstanceNotFoundException.class, () -> {
            Purchase purchase = gameService.purchaseGame(USER_EMAIL,NON_EXISTENT_GAME_ID, VALID_CREDIT_CARD_NUMBER,UNITS);
            removePurchase(purchase.getPurchaseId());
        });
    }

    @Test
    public void testBuyGameWithInvalidCreditCard() {
        Game game = createGame(getValidGame());
        try {
            assertThrows(InputValidationException.class, () -> {
                Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), INVALID_CREDIT_CARD_NUMBER, UNITS );
                removePurchase(purchase.getPurchaseId());
            });
        } finally {
            // Clear database
            removeGame(game.getGameId());
        }

    }

    @Test
    public void testInvalidPurchase(){
        // Check userEmail not null
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame(null, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

        // Check userEmail not null
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame("", game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

        //Check userEmail follows the patron user@domain.*
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame("isd@quefacil.klk", game.getGameId(), VALID_CREDIT_CARD_NUMBER, -1);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

        //Check units > 0
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, -1);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

    }

    @Test
    public void testGameAlreadyPlayed(){
        assertThrows(GameAlreadyPlayedException.class, () -> {
            Game game = createGame(getValidGame());
            LocalDateTime celebrationDate = LocalDateTime.of(2000,12,12,12,12,12).withNano(0);
            game.setCelebrationDate(celebrationDate);
            updateGame(game);
            Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });
    }

    @Test
    public void testNotEnoughTickets(){
        assertThrows(NotEnoughUnitsException.class, () -> {
            Game game = createGame(getValidGame());
            game.setSoldTickets(9999);
            updateGame(game);
            Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });
    }

    @Test
    public void testFindUserPurchases() throws InputValidationException, NotEnoughUnitsException, GameAlreadyPlayedException, InstanceNotFoundException {
        //Create a game
        LocalDateTime someDate = LocalDateTime.of(2030,12,12,12,12,12).withNano(0);
        Game theGame = createGame(getValidGame("Los mariajos",someDate));

        // Create a list of purchases
        List<Purchase> purchases = new LinkedList<>();

        Purchase purchase1 = gameService.purchaseGame("fernando@alonso.es", theGame.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS );
        purchases.add(purchase1);
        Purchase purchase2 = gameService.purchaseGame("messi@gol.facil", theGame.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS );
        purchases.add(purchase2);
        Purchase purchase3 = gameService.purchaseGame("messi@gol.facil", theGame.getGameId(), "0123456789012345", UNITS + 300);
        purchases.add(purchase3);
        Purchase purchase4 = gameService.purchaseGame("cortinas@cuchilla.terraja", theGame.getGameId(), "0123456789012345", UNITS);
        purchases.add(purchase4);

        try {

            List<Purchase> foundPurchases = gameService.findUserPurchases("messi@gol.facil");
            assertEquals(2,foundPurchases.size());
            assertEquals(purchases.get(1),foundPurchases.get(0));
            assertEquals(purchases.get(2), foundPurchases.get(1));

            foundPurchases = gameService.findUserPurchases("pokemon@legide.com");
            assertEquals(0,foundPurchases.size());

        } finally {
            // Clear Database
            for (Purchase purchase : purchases) {
                removePurchase(purchase.getPurchaseId());
            }
            removeGame(theGame.getGameId());
        }
    }

    @Test
    public void testFindWithInvalidUserEmail(){
        // Check userEmail not null
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame(null, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

        // Check userEmail not null
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame("", game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

        //Check userEmail follows the patron user@domain.*
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame("isd@quefacil.klk", game.getGameId(), VALID_CREDIT_CARD_NUMBER, -1);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });
    }

    @Test
    public void testPickedTickets() throws NotEnoughUnitsException, GameAlreadyPlayedException, InstanceNotFoundException, InputValidationException, TicketsAlreadyPicked, NotValidCreditCardException {
        Game game = createGame(getValidGame());
        Purchase purchase = null;
        try {
            purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            assertFalse(purchase.getPickedUp());
            gameService.pickedTickets(purchase.getPurchaseId(), purchase.getCreditCardNumber());
            Purchase foundPurchase = findPurchase(purchase.getPurchaseId());
            assertTrue(foundPurchase.getPickedUp());

        } finally {
            if(purchase != null)
                removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        }
    }

    @Test
    public void testInvalidPickedTickets(){
        // Check creditCard not null
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            gameService.pickedTickets(purchase.getPurchaseId(), null);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });
        // Check creditCard with invalid length
        assertThrows(InputValidationException.class, () -> {
            Game game = createGame(getValidGame());
            Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS);
            gameService.pickedTickets(purchase.getPurchaseId(), INVALID_CREDIT_CARD_NUMBER);
            removePurchase(purchase.getPurchaseId());
            removeGame(game.getGameId());
        });

    }

    @Test
    public void testPickedTicketsWithInvalidCreditCard() {
        Game game = createGame(getValidGame());
        try {
            assertThrows(NotValidCreditCardException.class, () -> {
                Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS );
                gameService.pickedTickets(purchase.getPurchaseId(),"1234567890123455" );
                removePurchase(purchase.getPurchaseId());
            });
        } finally {
            removeGame(game.getGameId());
        }

    }

    @Test
    public void testAlreadyPickedTickets() {
        Game game = createGame(getValidGame());
        try {
            assertThrows(TicketsAlreadyPicked.class, () -> {
                Purchase purchase = gameService.purchaseGame(USER_EMAIL, game.getGameId(), VALID_CREDIT_CARD_NUMBER, UNITS );
                purchase.setPickedUp(true);
                updatePurchase(purchase);
                gameService.pickedTickets(purchase.getPurchaseId(), purchase.getCreditCardNumber());
                removePurchase(purchase.getPurchaseId());
            });
        } finally {
            removeGame(game.getGameId());
        }

    }

    @Test
    public void testPickTicketsNonExistentGame(){
        Game game = createGame(getValidGame());
        try{
            assertThrows(InstanceNotFoundException.class, () -> {
                Purchase purchase = gameService.purchaseGame(USER_EMAIL,game.getGameId(), VALID_CREDIT_CARD_NUMBER,UNITS);
                gameService.pickedTickets(NON_EXISTENT_GAME_ID, VALID_CREDIT_CARD_NUMBER);
                removePurchase(purchase.getPurchaseId());
            });
        } finally {
            removeGame(game.getGameId());
        }
    }

}