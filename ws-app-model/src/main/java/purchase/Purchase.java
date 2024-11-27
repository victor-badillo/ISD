package purchase;

import java.time.LocalDateTime;

public class Purchase {
    private Long gameId;
    private Long purchaseId;  //Tambien lo usamos para recoger las entradas en taquilla
    private String userEmail;
    private String creditCardNumber;
    private int units; //int porque lo indica el enunciado
    private LocalDateTime purchaseDate;
    private Boolean pickedUp;  //Para saber si los usuarios han recogido las entradas

    public Purchase(Long gameId, String userEmail, String creditCardNumber, int units, LocalDateTime purchaseDate, boolean pickedUp) {
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.creditCardNumber = creditCardNumber;
        this.units = units;
        this.purchaseDate = purchaseDate;
        this.pickedUp = pickedUp;
    }

    public Purchase(Long purchaseId, Long gameId, String userEmail, String creditCardNumber, int units, LocalDateTime purchaseDate, boolean pickedUp) {
        this.purchaseId = purchaseId;
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.creditCardNumber = creditCardNumber;
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

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Boolean getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        if (units != purchase.units) return false;
        if (!gameId.equals(purchase.gameId)) return false;
        if (!purchaseId.equals(purchase.purchaseId)) return false;
        if (!userEmail.equals(purchase.userEmail)) return false;
        if (!creditCardNumber.equals(purchase.creditCardNumber)) return false;
        if (!purchaseDate.equals(purchase.purchaseDate)) return false;
        return pickedUp.equals(purchase.pickedUp);
    }

    @Override
    public int hashCode() {
        int result = gameId.hashCode();
        result = 31 * result + purchaseId.hashCode();
        result = 31 * result + userEmail.hashCode();
        result = 31 * result + creditCardNumber.hashCode();
        result = 31 * result + units;
        result = 31 * result + purchaseDate.hashCode();
        result = 31 * result + pickedUp.hashCode();
        return result;
    }
}