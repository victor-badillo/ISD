package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.thrift.ThriftPurchaseDto;
import java.util.ArrayList;
import java.util.List;

public class ClientPurchaseDtoToThriftPurchaseDtoConversor {
    public static ClientPurchaseDto toClientPurchaseDto(ThriftPurchaseDto purchase) {

        return new ClientPurchaseDto(
                purchase.getGameId(),
                purchase.getPurchaseId(),
                purchase.getUserEmail(),
                purchase.getLastFourDigits(),
                purchase.getUnits(),
                purchase.getPurchaseDate(),
                purchase.isPickedUp());
    }

    public static List<ClientPurchaseDto> toClientPurchaseDtos(List<ThriftPurchaseDto> purchase) {

        List<ClientPurchaseDto> clientPurchaseDtos = new ArrayList<>(purchase.size());

        for (ThriftPurchaseDto Purchase : purchase) {
            clientPurchaseDtos.add(toClientPurchaseDto(Purchase));
        }
        return clientPurchaseDtos;

    }
}
