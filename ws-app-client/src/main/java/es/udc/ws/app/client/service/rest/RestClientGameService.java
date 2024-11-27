package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import es.udc.ws.app.client.service.ClientGameService;
import es.udc.ws.app.client.service.dto.ClientGameDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientGameAlreadyPlayedException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.app.client.service.exceptions.ClientNotValidCreditCardException;
import es.udc.ws.app.client.service.exceptions.ClientTicketsAlreadyPickedException;
import es.udc.ws.app.client.service.rest.json.JsonToClientGameDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientPurchaseDtoConversor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientGameService implements ClientGameService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientGameService.endpointAddress";
    private String endpointAddress;

    @Override
    public ClientGameDto addGame(ClientGameDto game) throws InputValidationException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "games").
                    bodyStream(toInputStream(game), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientGameDtoConversor.toClientGameDto(response.getEntity().getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientGameDto> findGameByDate(String specifiedDate) throws InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "games?specifiedDate="
                            + URLEncoder.encode(specifiedDate, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientGameDtoConversor.toClientGameDtos(response.getEntity()
                    .getContent());

        }catch (InputValidationException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientGameDto findGameById(Long gameId) throws InstanceNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "games/"
                            + gameId).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientGameDtoConversor.toClientGameDto(response.getEntity()
                    .getContent());

        }catch (InstanceNotFoundException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPurchaseDto purchaseGame(String userEmail, Long gameId, String creditCardNumber, int units) throws InputValidationException, InstanceNotFoundException, ClientNotEnoughUnitsException, ClientGameAlreadyPlayedException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "purchases").
                    bodyForm(
                            Form.form().
                                    add("userEmail", userEmail).
                                    add("gameId", Long.toString(gameId)).
                                    add("creditCardNumber", creditCardNumber).
                                    add("units",Integer.toString(units)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientPurchaseDtoConversor.toClientPurchaseDto(
                    response.getEntity().getContent());

        } catch (InputValidationException | ClientNotEnoughUnitsException | ClientGameAlreadyPlayedException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPurchaseDto> findUserPurchases(String userEmail) throws InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "purchases?userEmail="
                            + URLEncoder.encode(userEmail, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPurchaseDtoConversor.toClientPurchaseDtos(response.getEntity()
                    .getContent());

        } catch (InputValidationException e){
            throw e;

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pickedTickets(Long purchaseId, String creditCardNumber) throws InstanceNotFoundException, ClientNotValidCreditCardException, ClientTicketsAlreadyPickedException, InputValidationException {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() +
                            "purchases/" + purchaseId + "/pickedTickets").
                    bodyForm(
                    Form.form().
                            add("creditCardNumber", creditCardNumber).
                            build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | ClientNotValidCreditCardException | ClientTicketsAlreadyPickedException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream toInputStream(ClientGameDto game) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientGameDtoConversor.toObjectNode(game));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
