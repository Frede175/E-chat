package Business;


import Acquaintence.*;
import Acquaintence.Event.ChangeChatEvent;
import Acquaintence.Event.MessageEvent;
import Business.Connection.HubConnect;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Connection.RestConnect;
import Business.Models.*;
import GUI.GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessFacade implements IBusinessFacade {

    private HubConnect hubConnect = new HubConnect();
    private RestConnect restConnect = new RestConnect();
    private List<Department> departments = null;
    private Department currentDepartment = null;
    private List<Chat> chats = null;
    private Chat currentChat = null;
    private LoginUser loginUser = null;
    private String token = null;



    public BusinessFacade() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
    }

    /* Listener methods */
    private void getMessage(MessageEvent event) {
        for(Chat chat : chats) {
            if(chat.getId() == event.getMessageIn().getChatId()) {
                if(chat.getMessages().isEmpty()) {
                    // TODO make new thread for downloading existing messages
                    getMessages(chat.getId());
                }
                chat.addMessage((MessageIn) event.getMessageIn());
                break;
            }
        }
    }

    @Override
    public ConnectionState login(String username, String password) {
        RequestResponse<String> temp = restConnect.login(username, password);
        if(temp.getConnectionState() == ConnectionState.SUCCESS) {
            token = temp.getResponse();
            hubConnect.connect(token);
            RequestResponse<LoginUser> data = restConnect.get(PathEnum.GetUserInfo, null, null, token);
            loginUser = data.getResponse();
            loginUser.initializePermissions();
            getDepartments();
            getChats();
        }
        return temp.getConnectionState();
    }

    @Override
    public void addUserToDepartment(User user, Department department) {
        restConnect.post(PathEnum.AddUserToDeparment, department.getId(), user.getId(), token);
    }


    @Override
    public RequestResponse<List<? extends IUser>> getUsers() {
        RequestResponse<List<User>> response = restConnect.get(PathEnum.GetUsers, loginUser.getSub(), null, token);
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    //TODO DM should not be added to currentDepartment, should be fixed by server
    @Override
    public RequestResponse<Chat> createDirectMessage(String name, IUser otherUser) {
        Chat chat = new Chat(name);
        RequestResponse<Chat> response = restConnect.post(PathEnum.CreateDirectMessage, otherUser.getId(), chat, token);
        setCurrentChat(response.getResponse().getId());
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<String> addUserToSpecificChat(String userId, IChat chat) {
        RequestResponse<String> response = restConnect.post(PathEnum.AddUserToChat, chat.getId(), userId, token);
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }



    @Override
    public void setCurrentChat(int chatId) {
        if(currentChat.getId() != chatId) {
            for (Chat tempchat : chats) {
                if(tempchat.getId() == chatId) {
                    currentChat = tempchat;
                    if(currentChat.getMessages().isEmpty()) {
                        getMessages();
                    }
                    EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
                    break;
                }
            }
        } else {
            System.out.println("currentchat var den samme");
        }
    }

    @Override
    public User createUser(String username, String password) {
        CreateUser userToSend = new CreateUser(username, password);
        RequestResponse<User> response = restConnect.post(PathEnum.CreateUser, null, userToSend, token);
        return response.getResponse();
    }

    @Override
    public ILoginUser getLoginUser() {
        return loginUser;
    }

    public RequestResponse<List<? extends IChat>> getChats() {
        RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetChats, loginUser.getSub(), currentDepartment.toMap(), token);
        if(!response.getResponse().isEmpty()) {
            currentChat = response.getResponse().get(0);
            chats = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
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
        RequestResponse<List<Department>> response = restConnect.get(PathEnum.GetDepartments, loginUser.getSub(),null,token);
        if(!response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            System.out.println("Current department = " + currentDepartment.getName());
            //TODO Delete this, fam
            //addUserToDepartment(createUser("Jeff14", "AdminAdmin123*"), currentDepartment);
            departments = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages(int chatId) {
        RequestResponse<List<MessageIn>> response = restConnect.get(PathEnum.GetMessages, chatId, new Page(0,20).toMap(), token);
        for(Chat chat : chats) {
            if(chat.getId() == chatId) {
                if (chat.getMessages().isEmpty()) {
                    chat.addMessages(response.getResponse());
                }
            }
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages() {

        if(currentChat != null) {
            RequestResponse<List<MessageIn>> response = restConnect.get(PathEnum.GetMessages, currentChat.getId(), new Page(0,20).toMap(), token);
            currentChat.addMessages(response.getResponse());
            return new RequestResponse<>(response.getResponse(), response.getConnectionState());
        }
        return new RequestResponse<>(new ArrayList<MessageIn>(), ConnectionState.SUCCESS);

    }

    @Override
    public void sendMessage(String message) {
        hubConnect.sendMessage(message, currentChat.getId());
    }

    @Override
    public void logout() {
        restConnect.logout(token);
        departments = null;
        currentDepartment = null;
        chats = null;
        currentChat = null;
        loginUser = null;
        token = null;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            GUI.getInstance().getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}