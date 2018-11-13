package Business;


import Acquaintence.*;
import Acquaintence.Event.MessageEvent;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.*;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private List<Department> departments;
    private Department currentDepartment;
    private List<Chat> chats;
    private Chat currentChat;
    private User user;
    private String token = null;



    public BusinessFacade() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
    }

    /* Listener methods */
    private void getMessage(MessageEvent event) {
        currentChat.addMessage((MessageIn) event.getMessageIn());
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
    public RequestResponse<List<? extends IChat>> getChats() {
        RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetChats, user.getSub(), currentDepartment.toMap(), token);
        if(!response.getResponse().isEmpty()) {
            currentChat = response.getResponse().get(0);
            chats = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }



    @Override
    public void setCurrentChat(int chatID) {
        if(currentChat.getId() != chatID) {
            for (Chat tempchat : chats) {
                if(tempchat.getId() == chatID) {
                    currentChat = tempchat;
                    break;
                }
                break;
            }
        } else {
            System.out.println("currentchat var den samme");
        }
    }

    @Override
    public void createUser(String username, String password) {
        CreateUser usertosend = new CreateUser(username,password);
        RequestResponse<User> requestResponse = restConnect.post(PathEnum.CreateUser,null, usertosend, token);
        User newuser = requestResponse.getResponse();
    }

    @Override
    public Chat getCurrentChat() {
        return currentChat;
    }

    @Override
    public RequestResponse<List<? extends IUser>> getUsersInChat() {
        return restConnect.get(PathEnum.GetUsersInChat, currentChat.getId(), null, token );
    }

    public RequestResponse<List<? extends IDepartment>> getDepartments() {
        RequestResponse<List<Department>> response = restConnect.get(PathEnum.GetDepartments, user.getSub(),null,token);
        if(!response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            departments = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
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
    public void sendMessage(String message) {
        hubConnect.sendMessage(message, currentChat.getId());
    }

}
