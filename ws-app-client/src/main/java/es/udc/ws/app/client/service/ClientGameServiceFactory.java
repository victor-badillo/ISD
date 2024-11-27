package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;
import java.lang.reflect.InvocationTargetException;

public class ClientGameServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientGameServiceFactory.className";
    private static Class<ClientGameService> serviceClass = null;

    private ClientGameServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientGameService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientGameService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientGameService getService() {

        try {
            return (ClientGameService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}