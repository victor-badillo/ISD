package game;


import java.time.LocalDateTime;

public class Game {
    private Long gameId;
    private String visitantName;
    private LocalDateTime celebrationDate;
    private double priceGame;
    private int ticketMaxCount;
    private LocalDateTime creationDate;
    private int soldTickets; //Atributo para realizar menos consultas a la base de datos

    public Game(Long gameId, String visitantName, LocalDateTime celebrationDate, double priceGame, int ticketMaxCount) {
        this.gameId = gameId;
        this.visitantName = visitantName;
        this.celebrationDate = celebrationDate;
        this.priceGame = priceGame;
        this.ticketMaxCount = ticketMaxCount;
    }

    public Game(Long gameId, String visitantName, LocalDateTime celebrationDate, double priceGame, int ticketMaxCount, LocalDateTime creationDate, int soldTickets) {
        this.gameId = gameId;
        this.visitantName = visitantName;
        this.celebrationDate = celebrationDate;
        this.priceGame = priceGame;
        this.ticketMaxCount = ticketMaxCount;
        this.creationDate = creationDate;
        this.soldTickets = soldTickets;
    }

    public Game(String visitantName, LocalDateTime celebrationDate, double priceGame, int ticketMaxCount) {
        this.visitantName = visitantName;
        this.celebrationDate = celebrationDate;
        this.priceGame = priceGame;
        this.ticketMaxCount = ticketMaxCount;
    }

    public Game(Long gameId, String visitantName, LocalDateTime celebrationDate, double priceGame, int ticketMaxCount, int soldTickets) {
        this.gameId=gameId;
        this.visitantName = visitantName;
        this.celebrationDate = celebrationDate;
        this.priceGame = priceGame;
        this.ticketMaxCount = ticketMaxCount;
        this.soldTickets=soldTickets;
    }



    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getVisitantName() {
        return visitantName;
    }

    public void setVisitantName(String visitantName) {
        this.visitantName = visitantName;
    }

    public LocalDateTime getCelebrationDate() {
        return celebrationDate;
    }

    public void setCelebrationDate(LocalDateTime celebrationDate) {
        this.celebrationDate = celebrationDate;
    }

    public double getPriceGame() {
        return priceGame;
    }

    public void setPriceGame(double priceGame) {
        this.priceGame = priceGame;
    }

    public int getTicketMaxCount() {
        return ticketMaxCount;
    }

    public void setTicketMaxCount(int ticketMaxCount) {
        this.ticketMaxCount = ticketMaxCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (Double.compare(priceGame, game.priceGame) != 0) return false;
        if (ticketMaxCount != game.ticketMaxCount) return false;
        if (soldTickets != game.soldTickets) return false;
        if (!gameId.equals(game.gameId)) return false;
        if (!visitantName.equals(game.visitantName)) return false;
        if (!celebrationDate.equals(game.celebrationDate)) return false;
        return creationDate.equals(game.creationDate);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = gameId.hashCode();
        result = 31 * result + visitantName.hashCode();
        result = 31 * result + celebrationDate.hashCode();
        temp = Double.doubleToLongBits(priceGame);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + ticketMaxCount;
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + soldTickets;
        return result;
    }

}
