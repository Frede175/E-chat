package Business;


import Acquaintence.*;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.Department;
import Business.Models.Page;
import Business.Models.User;

import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private List<Department> departments;
    private Department currentDepartment;
    private String token = null;
    private User user;
    private Chat chat;
    private Chat currentChat;
    private List<Chat> chats;

    @Override
    public void injectMessageReceiver(IMessageReceiver messageReceiver) {

    }


    @Override
    public RequestResponse<List<? extends IChat>> getChats() {
        RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetChats, user.getSub(), currentDepartment.toMap(), token);

        if(!response.getResponse().isEmpty()) {
            currentChat = response.getResponse().get(0);
            chats = response.getResponse();
            System.out.println(currentChat + " " + chats);
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }


    public RequestResponse<List<? extends IDepartment>> getDepartments() {
        RequestResponse<List<Department>> response = restConnect.get(PathEnum.GetDepartments, user.getSub(),null,token);

        if(!response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            departments = response.getResponse();
            System.out.println(currentDepartment + " " + departments);
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public void setCurrentChat(int chatID) {
        if(currentChat.getID() != chatID) {
            currentChat = chats.get(chatID);
            System.out.println("currentchat blev ændret");
        }
        System.out.println("currentchat var den samme");
    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, 1);
    }

    @Override
    public RequestResponse<List<? extends IUser>> getUsersInChat() {
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
            getDepartments();
        }
        return temp.getConnectionState();
    }

    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages() {
        return restConnect.get(PathEnum.GetMessages, currentChat.getID(), new Page(0,20).toMap(), token);
    }

}
