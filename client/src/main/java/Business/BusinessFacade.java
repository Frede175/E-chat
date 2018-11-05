package Business;

import Acquaintence.ConnectionState;
import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Acquaintence.IUser;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
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
    private User user;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public <T> RequestResponse<T> getChats(HashMap<String, String> param) {
        return restConnect.get(PathEnum.GetChats, user.getID(), param, token);
    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, 1);
    }

    @Override
    public ConnectionState login(String username, String password) {
        RequestResponse<String> temp = restConnect.login(username, password);
        if(temp.getConnectionState() == ConnectionState.SUCCESS) {
            token = temp.getResponse();
            hubConnect.connect(token);
            RequestResponse<User> data = restConnect.get(PathEnum.GetUserInfo, null, null, token);
            user = data.getResponse();
            System.out.println(user.getName());

        }
        return temp.getConnectionState();
    }
}
