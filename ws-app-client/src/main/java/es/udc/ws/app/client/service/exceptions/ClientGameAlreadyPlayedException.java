package es.udc.ws.app.client.service.exceptions;

@SuppressWarnings("serial")
public class ClientGameAlreadyPlayedException extends Exception{
    private Long gameId;
    private String celebrationDate;

    public ClientGameAlreadyPlayedException (Long gameId, String celebrationDate) {
        super("Game with id= \""+ gameId +
                "\" was already played on date = \"" +
                celebrationDate + "\"");
        this.gameId = gameId;
        this.celebrationDate = celebrationDate;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getCelebrationDate() {
        return celebrationDate;
    }

    public void setCelebrationDate(String celebrationDate) {
        this.celebrationDate = celebrationDate;
    }
}