package Business;

import Acquaintence.IBusinessFacade;
import Acquaintence.IMessageReceiver;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.CreateUser;

import java.util.HashMap;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private String token;


    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }

    @Override
    public <T> T get(PathEnum path, String route, HashMap<String, String> param, String token) {
        return restConnect.get(path, route, param, token);

    }

    @Override
    public void addDummyData(String token) {
        String result = restConnect.post(PathEnum.CreateUser, null, new CreateUser("admin1", "Password123*"), null);
        String result1 = restConnect.post(PathEnum.CreateChatroom, 1, new Chat(1, "ChatTEST"), token);
        System.out.println("User created");
        //String jeff = restConnect.post(PathEnum.AddUserToChat, 1, "d8d65767-ca69-4abb-974e-a21883096b4e", token);
        System.out.println("User added");
    }

    @Override
    public String login(String username, String password) {
        token = restConnect.login(username, password);
        hubConnect.connect(token);
        return token;
    }
}
