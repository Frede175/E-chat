package Business;

import Acquaintence.*;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.Page;
import Business.Models.Department;
import Business.Models.User;

import java.util.HashMap;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private List<Department> departments;
    private Department currentdepartment;
    private String token = null;
    private User user;

    private Chat chat;
    private Chat currentChat;
    private List<Chat> chats;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }


    @Override
    public RequestResponse<List<IChat>> getChats() {
        RequestResponse<List<IChat>> response = restConnect.get(PathEnum.GetChats, user.getSub(), currentdepartment.toMap(), token);
        currentChat = (Chat) response.getResponse().get(0);

        return response;
    }

    public RequestResponse<List<IDepartment>> getDepartments() {
        RequestResponse<List<IDepartment>> response = restConnect.get(PathEnum.GetDepartments, user.getSub(),null,token);
        currentdepartment = (Department) response.getResponse().get(0);

        return response;
    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, 1);
    }

    @Override
    public <T> RequestResponse<T> getUsersInChat() {
        return restConnect.get(PathEnum.GetUsersInChat, currentChat.getID(), null, token );
    }

    @Override
    public ConnectionState login(String username, String password) {
        RequestResponse<String> temp = restConnect.login(username, password);
        if(temp.getConnectionState() == ConnectionState.SUCCESS) {
            token = temp.getResponse();
            hubConnect.connect(token);
            RequestResponse<User> data = restConnect.get(PathEnum.GetUserInfo, null, null, token);
            user = data.getResponse();
        }
        return temp.getConnectionState();
    }

    public <T> RequestResponse<T> getMessages() {
        return restConnect.get(PathEnum.GetMessages, chat.getID(), new Page(0,20).toMap(), token);
    }

}
