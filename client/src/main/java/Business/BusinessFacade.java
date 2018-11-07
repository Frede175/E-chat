package Business;


import Acquaintence.*;
import Acquaintence.Event.MessageEvent;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.*;

import java.util.ArrayList;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private List<Department> departments;
    private Department currentDepartment;
    private String token = null;
    private User user;
    private Chat currentChat;
    private List<Chat> chats;


    public BusinessFacade() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
    }

    private void getMessage(MessageEvent event) {
        currentChat.addMessage((MessageIn) event.getMessageIn());
    }

    @Override
    public void injectGUINotifier(IGUINotifier guiNotifier) {
        hubConnect.injectGUINotifier(guiNotifier);
        hubConnect.injectBusinessFacade(this);
    }


    @Override
    public RequestResponse<List<? extends IChat>> getChats() {
        System.out.println("Det her er result koden");
        RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetChats, user.getSub(), currentDepartment.toMap(), token);
        System.out.println(response.getResponse());
        System.out.println("ID'et er " + response.getResponse().get(0).getId() + " når det kommer over fra serveren");
        if(!response.getResponse().isEmpty()) {
            currentChat = response.getResponse().get(0);
            chats = response.getResponse();
            System.out.println("currentChats ID " + currentChat.getId());
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
        if(currentChat.getId() != chatID) {
            for (Chat tempchat: chats) {
                if(tempchat.getId() == chatID) {
                    currentChat = tempchat;
                    System.out.println("currentchat blev ændret til chatten med id'et " + tempchat.getId());
                    break;
                }
                break;
            }
        } else {
            System.out.println("currentchat var den samme");
        }
    }

    @Override
    public void sendMessage(String message) {
        //TODO Place ChatID somewhere/refactor
        hubConnect.sendMessage(message, currentChat.getId());
    }

    @Override
    public RequestResponse<List<? extends IUser>> getUsersInChat() {
        return restConnect.get(PathEnum.GetUsersInChat, currentChat.getId(), null, token );
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
        RequestResponse<List<MessageIn>> response = restConnect.get(PathEnum.GetMessages, currentChat.getId(), new Page(0,20).toMap(), token);
        if(currentChat != null) {
            currentChat.addMessages(response.getResponse());
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public Chat getCurrentChat() {
        return currentChat;
    }

}
