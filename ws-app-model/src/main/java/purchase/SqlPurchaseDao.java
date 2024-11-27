package purchase;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlPurchaseDao {
    public Purchase create(Connection connection, Purchase purchase);

    public List<Purchase> findUserPurchases(Connection connection, String useremail);

    public Purchase find(Connection connection, Long purchaseId) throws InstanceNotFoundException;

    public void updatePurchase(Connection connection, Purchase purchase) throws InstanceNotFoundException;

    // Necesario para las pruebas
    public void remove(Connection connection, Long purchaseId) throws InstanceNotFoundException;
}
