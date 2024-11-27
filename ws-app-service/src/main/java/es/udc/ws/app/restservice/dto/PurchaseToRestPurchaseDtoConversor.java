package es.udc.ws.app.restservice.dto;

import java.util.ArrayList;
import java.util.List;

import purchase.Purchase;

public class PurchaseToRestPurchaseDtoConversor {

    public static List<RestPurchaseDto> toRestPurchaseDtos(List<Purchase> purchases) {
        List<RestPurchaseDto> purchaseDtos = new ArrayList<>(purchases.size());
        for(int i = 0; i < purchases.size(); i++) {
            Purchase purchase = purchases.get(i);
            purchaseDtos.add(toRestPurchaseDto(purchase));
        }
        return purchaseDtos;
    }
    public static RestPurchaseDto toRestPurchaseDto(Purchase purchase) {
        return new RestPurchaseDto(purchase.getGameId(), purchase.getPurchaseId(), purchase.getUserEmail(),
                purchase.getCreditCardNumber().substring(purchase.getCreditCardNumber().length() - 4), purchase.getUnits(), purchase.getPurchaseDate().toString(),purchase.getPickedUp());
    }

}