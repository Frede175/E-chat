package Business;

import Acquaintence.ConnectionState;
import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.User;

import java.util.HashMap;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();

    private String token = null;
    private User user;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public <T> T getChats(HashMap<String, String> param) {
        System.out.println(user.getSub());
        return restConnect.get(PathEnum.GetChats, user.getSub(), param, token);
    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, 1);
    }

    @Override
    public ConnectionState login(String username, String password) {
        String temp = restConnect.login(username, password);
        if(temp.equals("error")) {
            return ConnectionState.WRONG_LOGIN;
        } else if(temp.equals("noConnection")) {
            return ConnectionState.NO_CONNECTION;
        } else {
            token = temp;
            hubConnect.connect(token);
            user = restConnect.get(PathEnum.GetUserInfo, null, null, token);
            System.out.println(user.getName());
            return ConnectionState.SUCCESS;
        }
    }
}
