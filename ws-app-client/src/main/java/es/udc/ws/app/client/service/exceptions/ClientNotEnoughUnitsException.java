package es.udc.ws.app.client.service.exceptions;

@SuppressWarnings("serial")
public class ClientNotEnoughUnitsException extends Exception{

    private int units;
    private int soldTickets;
    private Long gameId;

    public ClientNotEnoughUnitsException ( Long gameId, int units, int soldTickets) {
        super("Game with id= \""+ gameId +
                "\" has not enough tickets to make the purchase, there are = \"" +
                soldTickets + " sold tickets\", number of units impossible to purchase =\"" + units+ "\"");
        this.gameId = gameId;
        this.units = units;
        this.soldTickets = soldTickets;
    }

    public Long getGameId(){ return gameId;}

    public  void setGameId(Long gameId){ this.gameId = gameId;}

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }
}