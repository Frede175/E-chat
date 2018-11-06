package Business;

import Acquaintence.ConnectionState;
import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.Page;
import Business.Models.User;

import java.util.HashMap;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();

    private String token = null;
    private User user;
    private Chat chat;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public <T> RequestResponse<T> getChats(HashMap<String, String> param) {
        return restConnect.get(PathEnum.GetChats, user.getSub(), param, token);
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

    public <T> RequestResponse<T> getMessages() {
        return restConnect.get(PathEnum.GetMessages, chat.getID(), new Page(0,20).toMap(), token);
    }

}
