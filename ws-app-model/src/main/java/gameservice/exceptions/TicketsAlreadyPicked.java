package gameservice.exceptions;


public class TicketsAlreadyPicked extends Exception{
    private Long purchaseId;

    public TicketsAlreadyPicked(Long purchaseId) {
        super("Purchase with purchase Id = \""+ purchaseId +
                "\" tickets were already picked up");
        this.purchaseId=purchaseId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

}