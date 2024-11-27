package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftPurchaseDto;
import purchase.Purchase;
import java.util.ArrayList;
import java.util.List;

public class PurchaseToThriftPurchaseDtoConversor {

    public static List<ThriftPurchaseDto> toThriftPurchaseDtos(List<Purchase> purchases) {

        List<ThriftPurchaseDto> dtos = new ArrayList<>(purchases.size());

        for (Purchase purchase : purchases) {
            dtos.add(toThriftPurchaseDto(purchase));
        }
        return dtos;

    }

    public static ThriftPurchaseDto toThriftPurchaseDto(Purchase purchase) {

        return new ThriftPurchaseDto(purchase.getGameId(), purchase.getPurchaseId(), purchase.getUserEmail(),
                purchase.getCreditCardNumber().substring(purchase.getCreditCardNumber().length() - 4),
                purchase.getUnits(), purchase.getPurchaseDate().toString(), purchase.getPickedUp());

    }

}