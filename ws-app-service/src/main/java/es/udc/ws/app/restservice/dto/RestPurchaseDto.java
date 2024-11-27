package es.udc.ws.app.restservice.dto;

public class RestPurchaseDto {
    private Long gameId;
    private Long purchaseId;
    private String userEmail;
    private String lastFourDigits;
    private int units;
    private String purchaseDate;
    private Boolean pickedUp;

    public RestPurchaseDto(){}


    public RestPurchaseDto(Long gameId, Long purchaseId, String userEmail, String lastFourDigits, int units, String purchaseDate, Boolean pickedUp) {
        this.gameId = gameId;
        this.purchaseId = purchaseId;
        this.userEmail = userEmail;
        this.lastFourDigits = lastFourDigits;
        this.units = units;
        this.purchaseDate = purchaseDate;
        this.pickedUp = pickedUp;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Boolean getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }
}