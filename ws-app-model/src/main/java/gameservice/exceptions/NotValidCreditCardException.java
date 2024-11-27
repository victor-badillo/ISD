package gameservice.exceptions;


public class NotValidCreditCardException extends Exception{
    private String creditCardNumber;
    private Long purchaseId;

    public NotValidCreditCardException (String creditCardNumber, Long purchaseId) {
        super("Trying to pick tickets with credit card number= \""+ creditCardNumber +
                "\" which is different from the credit card number of the purchase with Id = \"" +
                purchaseId + "\"");
        this.creditCardNumber=creditCardNumber;
        this.purchaseId=purchaseId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

}