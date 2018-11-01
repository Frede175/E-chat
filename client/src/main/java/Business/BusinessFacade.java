package Business;

import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Acquaintence.IUser;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.CreateUser;
import Business.Models.User;

import java.util.HashMap;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();

    private String token = null;
    private User user = new User("d8d65767-ca69-4abb-974e-a21883096b4e", "Jeff");

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public <T> T getChats(HashMap<String, String> param) {
        return restConnect.get(PathEnum.GetChats, user.getID(), param, token);

    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, 1);
    }

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
