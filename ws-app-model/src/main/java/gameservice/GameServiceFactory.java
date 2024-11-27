package gameservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class GameServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "GameServiceFactory.className";
    private static GameService service = null;
    private GameServiceFactory() {
    }
    @SuppressWarnings("rawtypes")
    private static GameService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (GameService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static GameService getService() {
        if (service == null) {
            service = getInstance();
        }
        return service;

    }

}

