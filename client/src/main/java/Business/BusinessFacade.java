package Business;

import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Business.Connection.HubConnect;
import Business.Connection.RestConnect;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public void login(String username, String password) {
        String token = restConnect.login(username, password);
        hubConnect.connect(token);
    }
}
