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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        chats.add(response.getResponse());
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<String> addUserToSpecificChat(String userId, IChat chat) {
        RequestResponse<String> response = restConnect.post(PathEnum.AddUserToChat, chat.getId(), userId, token);
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }



    @Override
    public void setCurrentChat(int chatId) {
        if(currentChat != null) {
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
        } else if (chats != null) {
            currentChat = chats.get(0);
            if(currentChat.getMessages().isEmpty()) {
                getMessages();
            }
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }

    }

    @Override
    public void createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds) {
        CreateUser userToSend = new CreateUser(username, password, role.getName(), departmentsIds);
        restConnect.post(PathEnum.CreateUser, null, userToSend, token);
    }

    //new
    public void createChat(String chatname, int departmentId){
        Chat chatToSend = new Chat(chatname);
        RequestResponse<Chat> response = restConnect.post(PathEnum.CreateChatroom, departmentId, chatToSend, token);
        chats.add(response.getResponse());
    }

    public void createDepartment(String departmentName, int id){
        Department departmentToSend = new Department(id, departmentName);
        //RequestResponse<Department> response =
        restConnect.post(PathEnum.CreateDepartment, null, departmentToSend, token);
        //departments.add(response.getResponse());
    }

    @Override
    public void createUserRole(List<String> permissions, String roleName) {
        //TODO not tested, could change route and param
        restConnect.post(PathEnum.CreateUserRole, roleName, permissions, token);
    }

    @Override
    public ILoginUser getLoginUser() {
        return loginUser;
    }

    @Override

    public RequestResponse<List<? extends IRole>> getRoles() {
        // TODO Maybe dont make a request everytime
        return restConnect.get(PathEnum.GetRoles, null, null, token);
    }

    public void addRoleToUser(String userId, String role) {
        restConnect.put(PathEnum.AddRoleToUser, userId, role, token);
    }

    public RequestResponse<List<? extends IChat>> getChats() {
        RequestResponse<List<Chat>> departmentChats = restConnect.get(PathEnum.GetChats, loginUser.getSub(), currentDepartment.toMap(), token);
        RequestResponse<List<Chat>> privateChats = restConnect.get(PathEnum.GetDirectMessages, loginUser.getSub(), currentDepartment.toMap(), token);
        if(departmentChats.getResponse().isEmpty() || privateChats.getResponse().isEmpty()) {
            if(!departmentChats.getResponse().isEmpty()) {
                currentChat = departmentChats.getResponse().get(0);
                chats = departmentChats.getResponse();
                return new RequestResponse<>(departmentChats.getResponse(), departmentChats.getConnectionState());
            }
            if(!privateChats.getResponse().isEmpty()) {
                currentChat = privateChats.getResponse().get(0);
                chats = privateChats.getResponse();
                return new RequestResponse<>(privateChats.getResponse(), privateChats.getConnectionState());
            }
        } else {
            List<Chat> allChats = Stream.concat(privateChats.getResponse().stream(), privateChats.getResponse().stream()).collect(Collectors.toList());
            currentChat = allChats.get(0);
            chats = allChats;
            return new RequestResponse<>(allChats, ConnectionState.SUCCESS);
        }

        return new RequestResponse<>(departmentChats.getResponse(), departmentChats.getConnectionState());
    }

    @Override
    public Chat getCurrentChat() {
        return currentChat;
    }

    @Override
    public RequestResponse<List<? extends String>> getAllPermissions() {
        //TODO not tested, route could be something or param could be something
        RequestResponse<List<String>> response = restConnect.get(PathEnum.GetAllPermissions, null, null, token);
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public void deleteUserRole(String roleName) {
        restConnect.delete(PathEnum.DeleteUserRole, roleName, token);
    }

    @Override
    public RequestResponse<List<? extends IUser>> getUsersInChat() {
        return restConnect.get(PathEnum.GetUsersInChat, currentChat.getId(), null, token );
    }

    public RequestResponse<List<? extends IDepartment>> getDepartments() {
        RequestResponse<List<Department>> response = restConnect.get(PathEnum.GetDepartments, loginUser.getSub(),null,token);
        if(response.getResponse() != null && !response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            System.out.println("Current department = " + currentDepartment.getName());
            departments = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IDepartment>> getAllDepartments() {
        return restConnect.get(PathEnum.GetAllDepartments, null, null, token );
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
        hubConnect.disconnect();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            GUI.getInstance().getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}