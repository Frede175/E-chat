package Business;

import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.CreateUser;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public void addDummyData() {
        String result = restConnect.post(PathEnum.CreateUser, null, new CreateUser("username", "password"), null);
    }

    @Override
    public void login(String username, String password) {
        String token = restConnect.login(username, password);
        hubConnect.connect(token);
    }
}
