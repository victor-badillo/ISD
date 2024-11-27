package es.udc.ws.app.restservice.dto;

public class RestGameDto {
    private Long gameId;
    private String visitantName;
    private String celebrationDate;
    private double priceGame;
    private int ticketMaxCount;
    private int soldTickets;

    public RestGameDto(){}


    public RestGameDto(Long gameId, String visitantName, String celebrationDate, double priceGame, int ticketMaxCount, int soldTickets) {
        this.gameId = gameId;
        this.visitantName = visitantName;
        this.celebrationDate = celebrationDate;
        this.priceGame = priceGame;
        this.ticketMaxCount = ticketMaxCount;
        this.soldTickets = soldTickets;
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

    public String getCelebrationDate() {
        return celebrationDate;
    }

    public void setCelebrationDate(String celebrationDate) {
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

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    @Override
    public String toString() {
        return "RestGameDto [gameId=" + gameId + ", visitantName=" + visitantName +
                ", celebrationDate=" + celebrationDate + ", priceGame=" + priceGame +
                ", ticketMaxCount=" + ticketMaxCount + ", soldTickets=" + soldTickets + "]";
    }
}