package Business;

import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Acquaintence.IUser;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.CreateUser;
import Business.Models.User;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private String token = null;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public void addDummyData() {
        String result = restConnect.post(PathEnum.CreateUser, null, new CreateUser("admin", "Password123*"), null);
    }

    @Override
    public boolean login(String username, String password) {
        String temp = restConnect.login(username, password);
        System.out.println("Business: " + temp);
        if(!temp.equals("error")) {
            token = temp;
            hubConnect.connect(token);
            return true;
        } else {
            return false;
        }

    }
}
