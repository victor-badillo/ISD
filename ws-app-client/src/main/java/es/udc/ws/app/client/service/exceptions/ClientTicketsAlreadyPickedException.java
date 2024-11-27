package es.udc.ws.app.client.service.exceptions;

@SuppressWarnings("serial")
public class ClientTicketsAlreadyPickedException extends Exception{

    private Long purchaseId;

    public ClientTicketsAlreadyPickedException(Long purchaseId) {
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