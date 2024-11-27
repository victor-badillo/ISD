package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientGameService;
import es.udc.ws.app.client.service.dto.ClientGameDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientGameAlreadyPlayedException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientNotValidCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyPickedException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import java.util.List;

public class ThriftClientGameService implements ClientGameService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientGameService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);


    @Override
    public ClientGameDto addGame(ClientGameDto gameDto) throws InputValidationException {
        ThriftGameService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientGameDtoToThriftGameDtoConversor.toClientGameDto(client.addGame(ClientGameDtoToThriftGameDtoConversor.toThriftGameDto(gameDto)));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientGameDto> findGameByDate(String specifiedDate) throws InputValidationException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientGameDtoToThriftGameDtoConversor.toClientGameDtos(client.findGameByDate(specifiedDate));

        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientGameDto findGameById(Long gameId) throws InstanceNotFoundException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientGameDtoToThriftGameDtoConversor.toClientGameDto(client.findGameById(gameId));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPurchaseDto purchaseGame(String userEmail, Long gameId, String creditCardNumber, int units) throws InputValidationException, InstanceNotFoundException, ClientNotEnoughUnitsException, ClientGameAlreadyPlayedException {

        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();
            return ClientPurchaseDtoToThriftPurchaseDtoConversor.toClientPurchaseDto(client.purchaseGame(userEmail, gameId, creditCardNumber, units));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (ThriftGameAlreadyPlayedException e){
            throw new ClientGameAlreadyPlayedException(e.getGameId(),e.getCelebrationDate());
        }catch (ThriftNotEnoughUnitsException e){
            throw new ClientNotEnoughUnitsException(e.getGameId(), e.getSoldTickets(), e.getUnits());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPurchaseDto> findUserPurchases(String userEmail) throws InputValidationException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return ClientPurchaseDtoToThriftPurchaseDtoConversor.toClientPurchaseDtos(client.findUserPurchases(userEmail));
        }catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickedTickets(Long purchaseId, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ClientNotValidCreditCardException, ClientTicketsAlreadyPickedException {
        ThriftGameService.Client client = getClient();
        try( TTransport transport = client.getInputProtocol().getTransport() ){
            client.pickedTickets(purchaseId,creditCardNumber);
        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftNotValidCreditCardException e){
            throw new ClientNotValidCreditCardException(e.getCreditCardNumber(),e.getPurchaseId());
        } catch (ThriftTicketsAlreadyPickedException e){
            throw new ClientTicketsAlreadyPickedException(e.getPurchaseId());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private ThriftGameService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftGameService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}