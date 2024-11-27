package es.udc.ws.app.client.service.dto;

public class ClientGameDto {

    private Long gameId;
    private String visitantName;
    private String celebrationDate;
    private double priceGame;
    private int availableTickets;
    private int ticketMaxCount;

    public ClientGameDto(){}

    public ClientGameDto(Long gameId, String visitantName, String celebrationDate, double priceGame, int availableTickets, int ticketMaxCount){
        this.gameId=gameId;
        this.visitantName=visitantName;
        this.celebrationDate=celebrationDate;
        this.priceGame=priceGame;
        this.availableTickets=availableTickets;
        this.ticketMaxCount=ticketMaxCount;
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

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public int getTicketMaxCount() {
        return ticketMaxCount;
    }

    public void setTicketMaxCount(int ticketMaxCount) {
        this.ticketMaxCount = ticketMaxCount;
    }

    @Override
    public String toString() {
        return "GameDto [gameId=" + gameId + ", visitantName=" + visitantName
                + ", celebrationDate=" + celebrationDate + ", priceGame=" + priceGame
                + ", availableTickets=" + availableTickets + ", ticketMaxCount=" + ticketMaxCount + "]";
    }


}