package Business;


import Acquaintence.*;
import Acquaintence.Event.*;
import Business.Connection.*;
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
    private List<Department> departments = new ArrayList();
    private Department currentDepartment = null;
    private List<Chat> chats = new ArrayList();
    private Chat currentChat = null;
    private List<User> users = new ArrayList();
    private LoginUser loginUser = null;
    private String token = null;


    public BusinessFacade() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
        EventManager.getInstance().registerListener(NewChatEvent.class, this::getNewChat);
        EventManager.getInstance().registerListener(AddChatEvent.class, this::addChat);
        EventManager.getInstance().registerListener(RemoveUserFromChatEvent.class, this::removeUserFromChat);
        EventManager.getInstance().registerListener(LeaveChatEvent.class, this::leaveChatEvent);
        EventManager.getInstance().registerListener(DeleteChatEvent.class, this::deleteChatEvent);
    }


    /* Listener methods */
    private void getMessage(MessageEvent event) {
        for (Chat chat : chats) {
            if (chat.getId() == event.getMessageIn().getChatId()) {
                if (chat.getMessages().isEmpty()) {
                    getMessages(chat.getId());
                }
                chat.addMessage((MessageIn) event.getMessageIn());
                break;
            }
        }
    }

    private void leaveChatEvent(LeaveChatEvent leaveChatEvent) {
        if (leaveChatEvent.getUser().getId().equals(loginUser.getSub())) {
            chats.removeIf(chat -> chat.getId() == leaveChatEvent.getChatId());
        }
        if (!chats.isEmpty() && currentChat.getId() == leaveChatEvent.getChatId()) {
            setCurrentChat(chats.get(0).getId());
        } else {
            currentChat = null;
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }
        users.clear();
        users.addAll((List<User>) getUsers().getResponse());
    }

    private void removeUserFromChat(RemoveUserFromChatEvent removeUserFromChatEvent) {
        if (removeUserFromChatEvent.getUser().getId().equals(loginUser.getSub())) {
            chats.removeIf(chat -> chat.getId() == removeUserFromChatEvent.getChatId());
        }
        if (!chats.isEmpty() && currentChat.getId() == removeUserFromChatEvent.getChatId()) {
            setCurrentChat(chats.get(0).getId());
        } else {
            currentChat = null;
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }
        users.clear();
        users.addAll((List<User>) getUsers().getResponse());
    }

    private void addChat(AddChatEvent addChatEvent) {
        boolean found = false;
        for (Chat chat : chats) {
            if (chat.getId() == addChatEvent.getChatId()) {
                found = true;
                break;
            }
        }
        if (!found) {
            RequestResponse<Chat> chat = RestConnectBuilder.create(PathEnum.GetChat).withToken(token).withRoute(addChatEvent.getChatId()).build().execute();
            chats.add(chat.getResponse());
            if (users.contains(addChatEvent.getUser())) {
                RequestResponse<User> user = RestConnectBuilder.create(PathEnum.GetUser).withToken(token).withRoute(addChatEvent.getUser().getId()).build().execute();
                users.add(user.getResponse());
                EventManager.getInstance().fireEvent(new AddUserEvent(this, user.getResponse()));
            }
        }
    }

    private void getNewChat(NewChatEvent newChatEvent) {
        chats.add((Chat) newChatEvent.getChat());
    }

    private void deleteChatEvent(DeleteChatEvent deleteChatEvent) {
        chats.removeIf(c -> c.getId() == deleteChatEvent.getChatId());
        if (!chats.isEmpty() && currentChat.getId() == deleteChatEvent.getChatId()) {
            setCurrentChat(chats.get(0).getId());
        } else {
            currentChat = null;
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }
    }

    /*Chat Methods */
    @Override
    public RequestResponse<List<? extends IChat>> getChats() {
        RequestResponse<List<Chat>> chats = RestConnectBuilder.create(PathEnum.GetChats).withToken(token).withRoute(loginUser.getSub()).build().execute();

        if (chats.getResponse() != null && !chats.getResponse().isEmpty()) {
            for (Chat chat : chats.getResponse()) {
                if (chat.isGroupChat()) {
                    this.chats.add(chat);
                } else {
                    RequestResponse<List<? extends IUser>> response = RestConnectBuilder.create(PathEnum.GetUsersInChat).withToken(token).withRoute(chat.getId()).build().execute();
                    for (IUser user : response.getResponse()) {
                        if (!user.getName().equals(loginUser.getName())) {
                            chat.setName(user.getName());
                            this.chats.add(chat);
                        }
                    }
                }
            }
            setCurrentChat(this.chats.get(0).getId());
        }
        return new RequestResponse<>(chats.getResponse(), chats.getConnectionState());
    }

    @Override
    public List<? extends IChat> getExistingChats() {
        return chats;
    }

    @Override
    public Chat getCurrentChat() {
        return currentChat;
    }

    @Override
    public List<? extends IChat> getAvailableChats(String userId) {
        RequestResponse<List<Chat>> response = RestConnectBuilder.create(PathEnum.GetAvailableChats).withToken(token).withRoute(userId).build().execute();

        return response.getResponse();
    }

    @Override
    public List<? extends IChat> getUsersChats(String userId) {
        RequestResponse<List<Chat>> response = RestConnectBuilder.create(PathEnum.GetChats).withToken(token).withRoute(userId).build().execute();
        return response.getResponse();
    }

    @Override
    public RequestResponse<List<? extends IChat>> getAllChats() {
        return RestConnectBuilder.create(PathEnum.GetAllChats).withToken(token).build().execute();
    }

    @Override
    public ConnectionState createChat(String chatName, int departmentId) {
        Chat chatToSend = new Chat(chatName);
        RequestResponse<Chat> response = RestConnectBuilder.create(PathEnum.CreateChatroom).withToken(token).withRoute(departmentId).withContent(chatToSend).build().execute();
        return response.getConnectionState();
    }

    @Override
    public void leaveChat(int chatId){
        RestConnectBuilder.create(PathEnum.LeaveChat).withToken(token).withRoute(chatId).build().execute();
    }

    @Override
    public ConnectionState deleteChat(int chatId) {
        return RestConnectBuilder.create(PathEnum.RemoveChatroom).withToken(token).withRoute(chatId).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState addUserToChat(int chatId, String userId) {
        return RestConnectBuilder.create(PathEnum.AddUserToChat).withToken(token).withRoute(chatId).withContent(userId).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState removeUserFromChat(int chatId, String userId) {
        return RestConnectBuilder.create(PathEnum.RemoveUserFromChat).withToken(token).withRoute(chatId).withContent(userId).build().execute().getConnectionState();
    }

    @Override
    public void setCurrentChat(int chatId) {
        if (currentChat != null) {
            if (currentChat.getId() != chatId) {
                for (Chat tempchat : chats) {
                    if (tempchat.getId() == chatId) {
                        currentChat = tempchat;
                        if (currentChat.getMessages().isEmpty()) {
                            getMessages(0);
                        }
                        EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
                        break;
                    }
                }
            }
        } else if (chats != null && !chats.isEmpty()) {
            currentChat = chats.get(0);
            if (currentChat.getMessages().isEmpty()) {
                getMessages(0);
            }
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }

    }

    /*User Methods */
    @Override
    public RequestResponse<List<? extends IUser>> getUsers() {
        RequestResponse<List<User>> response = RestConnectBuilder.create(PathEnum.GetContacts).withToken(token).build().execute();
        if (response.getResponse() != null) {
            users = (response.getResponse());
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public List<? extends IUser> getExistingUsers() {
        return users;
    }

    @Override
    public RequestResponse<List<? extends IUser>> getUsersInChat() {
        return RestConnectBuilder.create(PathEnum.GetUsersInChat).withToken(token).withRoute(currentChat.getId()).build().execute();
    }

    @Override
    public ConnectionState createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds) {
        CreateUser userToSend = new CreateUser(username, password, role.getName(), departmentsIds);
        return RestConnectBuilder.create(PathEnum.CreateUser).withToken(token).withContent(userToSend).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState deleteUser(String userId) {
        return RestConnectBuilder.create(PathEnum.DeleteUser).withToken(token).withRoute(userId).build().execute().getConnectionState();
    }

    public ConnectionState addRoleToUser(String userId, String roleId) {
        return RestConnectBuilder.create(PathEnum.AddRoleToUser).withToken(token).withRoute(userId).withContent(roleId).build().execute().getConnectionState();

    }

    @Override
    public ConnectionState removeRoleFromUser(String userId, String roleId){
        return RestConnectBuilder.create(PathEnum.RemoveRoleFromUser).withToken(token).withRoute(userId).withContent(roleId).build().execute().getConnectionState();
    }

    /*Department Methods */
    @Override
    public RequestResponse<List<? extends IDepartment>> getDepartments() {
        RequestResponse<List<Department>> response = RestConnectBuilder.create(PathEnum.GetDepartmentsForUser).withToken(token).withRoute(loginUser.getSub()).build().execute();

        if (response.getResponse() != null && !response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            departments = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IDepartment>> getAllDepartments() {
        return RestConnectBuilder.create(PathEnum.GetAllDepartments).withToken(token).build().execute();
    }

    @Override
    public RequestResponse<List<IDepartment>> getAvailableDepartments(String userId) {
        return RestConnectBuilder.create(PathEnum.GetAvailableDepartments).withToken(token).withRoute(userId).build().execute();
    }

    @Override
    public RequestResponse<List<IUser>> getAllUsersInDepartment(int departmentId) {
        return RestConnectBuilder.create(PathEnum.GetAllUsersInDepartment).withToken(token).withRoute(departmentId).build().execute();
    }

    public ConnectionState createDepartment(String departmentName) {
        Department departmentToSend = new Department(departmentName);
        RequestResponse<Department> response = RestConnectBuilder.create(PathEnum.CreateDepartment).withToken(token).withContent(departmentToSend).build().execute();
        departments.add(response.getResponse());
        return response.getConnectionState();
    }

    public ConnectionState deleteDepartment(int departmentId) {
        return RestConnectBuilder.create(PathEnum.DeleteDepartment).withToken(token).withRoute(departmentId).build().execute().getConnectionState();
    }

    public ConnectionState updateDepartment(int departmentId, String name) {
        return RestConnectBuilder.create(PathEnum.UpdateDepartment).withToken(token).withRoute(departmentId).withContent(name).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState addUserToDepartment(int departmentId, String userId) {
        return RestConnectBuilder.create(PathEnum.AddUserToDeparment).withToken(token).withRoute(departmentId).withContent(userId).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState removeUserFromDepartment(String userId, int departmentId) {
        return RestConnectBuilder.create(PathEnum.RemoveUserFromDepartment).withToken(token).withRoute(departmentId).withContent(userId).build().execute().getConnectionState();
    }

    /*Message Methods */
    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages(int chatId, int page) {
        RequestResponse<List<MessageIn>> response = RestConnectBuilder.create(PathEnum.GetMessages).withToken(token).withRoute(chatId).withParameters(new Page(page, 20)).build().execute();
        for (Chat chat : chats) {
            if (chat.getId() == chatId) {
                if (chat.getMessages().isEmpty()) {
                    chat.addMessages(response.getResponse());
                }
            }
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages(int page) {
        if (currentChat == null) return null;
        return getMessages(currentChat.getId(), page);
    }

    @Override
    public void createDirectMessage(String name, String otherUserId) {
        Chat chat = new Chat(name);
        RequestResponse<Chat> response = RestConnectBuilder.create(PathEnum.CreateDirectMessage).withToken(token).withRoute(otherUserId).withContent(chat).build().execute();
        chats.add(response.getResponse());
    }

    @Override
    public void sendMessage(String message) {
        hubConnect.sendMessage(message, currentChat.getId());
    }

    /*Role Methods */
    @Override
    public RequestResponse<List<? extends IRole>> getRoles() {
        return RestConnectBuilder.create(PathEnum.GetRoles).withToken(token).build().execute();
    }

    @Override
    public RequestResponse<List<String>> getAllPermissions() {
        RequestResponse<List<String>> response = RestConnectBuilder.create(PathEnum.GetAllPermissions).withToken(token).build().execute();
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public List<String> getRolesPermissions(String roleid) {
        RequestResponse<List<String>> response = RestConnectBuilder.create(PathEnum.GetRolesPermissions).withToken(token).withRoute(roleid).build().execute();
        List<String> permissions = response.getResponse();
        return permissions;
    }

    @Override
    public ConnectionState createRole(List<String> permissions, String roleName) {
        return RestConnectBuilder.create(PathEnum.CreateRole).withToken(token).withRoute(roleName).withContent(permissions).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState deleteRole(String roleid) {
        return RestConnectBuilder.create(PathEnum.DeleteRole).withToken(token).withRoute(roleid).build().execute().getConnectionState();
    }

    @Override
    public ConnectionState addPermissionsToRole(String roleid, List<String> permissions) {
        return RestConnectBuilder.create(PathEnum.AddPermissionsToRole).withToken(token).withRoute(roleid).withContent(permissions).build().execute().getConnectionState();

    }
    @Override
    public RequestResponse<List<IRole>> getAvailableRoles(String userId){
        return RestConnectBuilder.create(PathEnum.GetAvailableRoles).withToken(token).withRoute(userId).build().execute();
    }


    public ConnectionState removePermissionsFromRole(String roleid, List<String> permissions) {
        return RestConnectBuilder.create(PathEnum.RemovePermissionsFromRole).withToken(token).withRoute(roleid).withContent(permissions).build().execute().getConnectionState();
    }

    /*Log Methods */
    @Override
    public RequestResponse<List<? extends ILogMessage>> getAllLogs(int page) {
        RequestResponse<List<LogMessage>> response = RestConnectBuilder.create(PathEnum.GetAllLogs).withToken(token).withParameters(new Page(page, 100)).build().execute();
        for(LogMessage logMessage : response.getResponse()) {
            logMessage.initializeLogLevel();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends ILogMessage>> getCustomLogs(int page) {
        RequestResponse<List<LogMessage>> response = RestConnectBuilder.create(PathEnum.GetCustomLogs).withToken(token).withParameters(new Page(page, 100)).build().execute();
        for(LogMessage logMessage : response.getResponse()) {
            logMessage.initializeLogLevel();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    /*Connection Methods */
    @Override
    public void logout() {
        RestConnectBuilder.create(PathEnum.Logout).withToken(token).build().execute();
        departments.clear();
        currentDepartment = null;
        chats.clear();
        currentChat = null;
        users.clear();
        loginUser = null;
        token = null;
        EventManager.getInstance().clearListeners();
        disconnectHub();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            GUI.getInstance().getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConnectionState login(String username, String password) {
        RequestResponse<Login> temp = RestConnectBuilder.create(PathEnum.Login).withContent(new LoginOut(username, password)).build().execute();
        if (temp.getConnectionState() == ConnectionState.SUCCESS) {
            token = temp.getResponse().getAccess_token();
            hubConnect.connect(token);
            RequestResponse<LoginUser> data = RestConnectBuilder.create(PathEnum.GetUserInfo).withToken(token).build().execute();
            loginUser = data.getResponse();
            loginUser.initializePermissions();
            getDepartments();
            getUsers();
            getChats();
            if (loginUser.getUserPermissions().isEmpty()) {
                return ConnectionState.NO_BASIC_PERMISSIONS;
            }
        }
        return temp.getConnectionState();
    }

    @Override
    public ILoginUser getLoginUser() {
        return loginUser;
    }

    @Override
    public void disconnectHub() {
        hubConnect.disconnect();
    }
}