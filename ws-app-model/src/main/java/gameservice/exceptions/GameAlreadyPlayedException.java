package gameservice.exceptions;


import java.time.LocalDateTime;

public class GameAlreadyPlayedException extends Exception{
    private Long gameId;
    private LocalDateTime celebrationDate;

    public GameAlreadyPlayedException (Long gameId, LocalDateTime celebrationDate) {
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

    public LocalDateTime getCelebrationDate() {
        return celebrationDate;
    }

    public void setCelebrationDate(LocalDateTime celebrationDate) {
        this.celebrationDate = celebrationDate;
    }

}