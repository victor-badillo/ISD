package game;


import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlGameDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlGameDaoFactory.className";
    private static SqlGameDao dao = null;

    private SqlGameDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlGameDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlGameDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlGameDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;
    }

}
