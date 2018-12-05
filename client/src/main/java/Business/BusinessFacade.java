package Business;


import Acquaintence.*;
import Acquaintence.Event.*;
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
        hubConnect.injectBusiness(this);
    }


    /* Listener methods */
    private void getMessage(MessageEvent event) {
        for (Chat chat : chats) {
            if (chat.getId() == event.getMessageIn().getChatId()) {
                if (chat.getMessages().isEmpty()) {
                    // TODO make new thread for downloading existing messages
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
            // RequestResponse<Chat> chat = restConnect.get(PathEnum.GetChat, addChatEvent.getChatId(), null, token);
            RequestResponse<Chat> chat = new RestConnect(PathEnum.GetChat,token).create().executeRoute(addChatEvent.getChatId());
            chats.add(chat.getResponse());
            if (users.contains(addChatEvent.getUser())) {
                // RequestResponse<User> user = restConnect.get(PathEnum.GetUser, addChatEvent.getUser().getId(), null, token);
                RequestResponse<User> user = new RestConnect(PathEnum.GetUser, token).create().executeRoute(addChatEvent.getUser().getId());
                users.add(user.getResponse());
                EventManager.getInstance().fireEvent(new AddUserEvent(this, user.getResponse()));
            }
        }
    }

    private void getNewChat(NewChatEvent newChatEvent) {
        chats.add((Chat) newChatEvent.getChat());
    }

    /*Chat Methods */
    @Override
    public RequestResponse<List<? extends IChat>> getChats() {
        // RequestResponse<List<Chat>> chats = restConnect.get(PathEnum.GetChats, loginUser.getSub(), null, token);
        RequestResponse<List<Chat>> chats = new RestConnect(PathEnum.GetChats,token).create().executeRoute(loginUser.getSub());
        if (chats.getResponse() != null && !chats.getResponse().isEmpty()) {
            for (Chat chat : chats.getResponse()) {
                if (chat.isGroupChat()) {
                    this.chats.add(chat);
                } else {
                    // Temporary polish fix
                    // RequestResponse<List<? extends IUser>> response = restConnect.get(PathEnum.GetUsersInChat, chat.getId(), null, token);
                    RequestResponse<List<? extends IUser>> response = new RestConnect(PathEnum.GetUsersInChat,token).create().executeRoute(chat.getId());
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

    // TODO Fix this aswell
    @Override
    public List<? extends IChat> getAvailableChats(String userId) {
        // RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetAvailableChats, userId, null, token);
        RequestResponse<List<Chat>> response = new RestConnect(PathEnum.GetAvailableChats,token).create().executeRoute(userId);
        return response.getResponse();
    }

    @Override
    public List<? extends IChat> getUsersChats(String userId) {
        // RequestResponse<List<Chat>> response = restConnect.get(PathEnum.GetChats, userId, null, token);
        RequestResponse<List<Chat>> response = new RestConnect(PathEnum.GetChats, token).create().executeRoute(userId);
        System.out.println(response.getResponse());
        return response.getResponse();
    }

    @Override
    public ConnectionState createChat(String chatName, int departmentId) {
        Chat chatToSend = new Chat(chatName);
        // RequestResponse<Chat> response = restConnect.post(PathEnum.CreateChatroom, departmentId, chatToSend, token);
        RequestResponse<Chat> response = new RestConnect(PathEnum.CreateChatroom, token).create().execute(departmentId, chatToSend);
        return response.getConnectionState();
    }

    @Override
    public void leaveChat(int chatId){
        // restConnect.post(PathEnum.LeaveChat, chatId, null, token);
        new RestConnect(PathEnum.LeaveChat, token).create().executeRoute(chatId);
    }

    @Override
    public void deleteChat(int chatId){
        // restConnect.delete(PathEnum.RemoveChatroom, chatId, token);
        new RestConnect(PathEnum.RemoveChatroom, token).create().executeRoute(chatId);
    }

    @Override
    public ConnectionState addUserToChat(int chatId, String userId) {
        //return restConnect.post(PathEnum.AddUserToChat, chatId, userId, token).getConnectionState();
        return new RestConnect(PathEnum.AddUserToChat, token).create().execute(chatId,userId).getConnectionState();
    }

    @Override
    public void removeUserFromChat(int chatId, String userId) {
        // restConnect.post(PathEnum.RemoveUserFromChat, chatId, userId, token);
        new RestConnect(PathEnum.RemoveUserFromChat, token).create().execute(chatId, userId);
    }

    @Override
    public void setCurrentChat(int chatId) {
        if (currentChat != null) {
            if (currentChat.getId() != chatId) {
                for (Chat tempchat : chats) {
                    if (tempchat.getId() == chatId) {
                        currentChat = tempchat;
                        if (currentChat.getMessages().isEmpty()) {
                            getMessages();
                        }
                        EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
                        break;
                    }
                }
            }
        } else if (chats != null && !chats.isEmpty()) {
            currentChat = chats.get(0);
            if (currentChat.getMessages().isEmpty()) {
                getMessages();
            }
            EventManager.getInstance().fireEvent(new ChangeChatEvent(this, currentChat));
        }

    }

    /*User Methods */
    @Override
    public RequestResponse<List<? extends IUser>> getUsers() {
        // RequestResponse<List<User>> response = restConnect.get(PathEnum.GetContacts, null, null, token);
        RequestResponse<List<User>> response = new RestConnect(PathEnum.GetAllUsers,token).create().executeNoParameters();
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
        // return restConnect.get(PathEnum.GetUsersInChat, currentChat.getId(), null, token);
        return new RestConnect(PathEnum.GetUsersInChat, token).create().executeRoute(currentChat.getId());
    }

    @Override
    public void createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds) {
        CreateUser userToSend = new CreateUser(username, password, role.getName(), departmentsIds);
        // restConnect.post(PathEnum.CreateUser, null, userToSend, token);
        new RestConnect(PathEnum.CreateUser, token).create().executeContent(userToSend);
    }

    @Override
    public void deleteUser(String userId) {
        // restConnect.delete(PathEnum.DeleteUser, userId, token);
        new RestConnect(PathEnum.DeleteUser, token).create().executeRoute(userId);
    }

    public void addRoleToUser(String userId, String rolename) {
        // restConnect.put(PathEnum.AddRoleToUser, userId, rolename, token);
        System.out.println(userId);
        new RestConnect(PathEnum.AddRoleToUser, token).create().execute(userId, rolename);
    }

    /*Department Methods */
    @Override
    public RequestResponse<List<? extends IDepartment>> getDepartments() {
        // RequestResponse<List<Department>> response = restConnect.get(PathEnum.GetDepartments, loginUser.getSub(), null, token);
        RequestResponse<List<Department>> response = new RestConnect(PathEnum.GetDepartments, token).create().executeRoute(loginUser.getSub());
        if (response.getResponse() != null && !response.getResponse().isEmpty()) {
            currentDepartment = response.getResponse().get(0);
            departments = response.getResponse();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public RequestResponse<List<? extends IDepartment>> getAllDepartments() {
        // return restConnect.get(PathEnum.GetAllDepartments, null, null, token);
        return new RestConnect(PathEnum.GetAllDepartments, token).create().executeNoParameters();
    }

    @Override
    public RequestResponse<List<IDepartment>> getAvailableDepartments(String userId) {
        // return restConnect.get(PathEnum.GetAvailableDepartments, userId, null, token);
        return new RestConnect(PathEnum.GetAvailableDepartments, token).create().executeRoute(userId);
    }

    @Override
    public RequestResponse<List<IUser>> getAllUsersInDepartment(int departmentId) {
        // return restConnect.get(PathEnum.GetAllUsersInDepartment, departmentId, null, token);
        return new RestConnect(PathEnum.GetAllUsersInDepartment, token).create().executeRoute(departmentId);
    }

    public void createDepartment(String departmentName) {
        Department departmentToSend = new Department(departmentName);
        // RequestResponse<Department> response = restConnect.post(PathEnum.CreateDepartment, null, departmentToSend, token);
        RequestResponse<Department> response = new RestConnect(PathEnum.CreateDepartment, token).create().executeContent(departmentToSend);
        departments.add(response.getResponse());
    }

    public void deleteDepartment(int departmentId) {
        // restConnect.delete(PathEnum.DeleteDepartment, departmentId, token);
        new RestConnect(PathEnum.DeleteDepartment, token).create().executeRoute(departmentId);
    }

    public void updateDepartment(int departmentId, String name) {
        //restConnect.put(PathEnum.UpdateDepartment, departmentId, name, token);
        new RestConnect(PathEnum.LeaveChat.UpdateDepartment).create().execute(departmentId, name);
    }

    @Override
    public void addUserToDepartment(int departmentId, String userId) {
        // restConnect.post(PathEnum.AddUserToDeparment, departmentId, userId, token);
        new RestConnect(PathEnum.AddUserToDeparment, token).create().execute(departmentId, userId);
    }

    @Override
    public void removeUserFromDepartment(String userId, int departmentId) {
        // restConnect.post(PathEnum.RemoveUserFromDepartment, departmentId, userId, token);
        new RestConnect(PathEnum.LeaveChat.RemoveUserFromDepartment, token).create().execute(departmentId, userId);
    }

    /*Message Methods */
    @Override
    public RequestResponse<List<? extends IMessageIn>> getMessages(int chatId) {
        // RequestResponse<List<MessageIn>> response = restConnect.get(PathEnum.GetMessages, chatId, new Page(0, 20).toMap(), token);
        RequestResponse<List<MessageIn>> response = new RestConnect(PathEnum.GetMessages, token).create().execute(chatId, new Page(0, 20).toMap());
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
    public RequestResponse<List<? extends IMessageIn>> getMessages() {
        if (currentChat == null) return null;
        return getMessages(currentChat.getId());
    }

    @Override
    public void createDirectMessage(String name, String otherUserId) {
        Chat chat = new Chat(name);
        // RequestResponse<Chat> response = restConnect.post(PathEnum.CreateDirectMessage, otherUser.getId(), chat, token);
        RequestResponse<Chat> response = new RestConnect(PathEnum.CreateDirectMessage, token).create().execute(otherUserId, chat);
        chats.add(response.getResponse());
    }

    @Override
    public void sendMessage(String message) {
        hubConnect.sendMessage(message, currentChat.getId());
    }

    /*Role Methods */
    @Override
    public RequestResponse<List<? extends IRole>> getRoles() {
        // return restConnect.get(PathEnum.GetRoles, null, null, token);
        return new RestConnect(PathEnum.GetRoles, token).create().executeNoParameters();
    }

    @Override
    public RequestResponse<List<String>> getAllPermissions() {
        // RequestResponse<List<String>> response = restConnect.get(PathEnum.GetAllPermissions, null, null, token);
        RequestResponse<List<String>> response = new RestConnect(PathEnum.GetAllPermissions, token).create().executeNoParameters();
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    @Override
    public List<String> getRolesPermissions(String roleid) {
        // RequestResponse<List<String>> response = restConnect.get(PathEnum.GetRolesPermissions, roleid, null, token);
        RequestResponse<List<String>> response = new RestConnect(PathEnum.GetRolesPermissions, token).create().executeRoute(roleid);
        List<String> permissions = response.getResponse();
        return permissions;
    }

    @Override
    public void createRole(List<String> permissions, String roleName) {
        // restConnect.post(PathEnum.CreateRole, roleName, permissions, token);
        new RestConnect(PathEnum.CreateRole, token).create().execute(roleName, permissions);
    }

    @Override
    public void deleteRole(String roleid) {
        // restConnect.delete(PathEnum.DeleteRole, roleid, token);
        new RestConnect(PathEnum.DeleteRole, token).create().executeRoute(roleid);
    }

    @Override
    public void addPermissionsToRole(String roleid, List<String> permissions) {
        // restConnect.post(PathEnum.AddPermissionsToRole, roleid, permissions, token);
        new RestConnect(PathEnum.AddPermissionsToRole, token).create().execute(roleid, permissions);
    }

    public void removePermissionsFromRole(String roleid, List<String> permissions) {
        // restConnect.post(PathEnum.RemovePermissionsFromRole, roleid, permissions, token);
        new RestConnect(PathEnum.RemovePermissionsFromRole, token).create().execute(roleid, permissions);
    }

    /*Log Methods */
    public RequestResponse<List<? extends ILogMessage>> getAllLogs() {
        // RequestResponse<List<LogMessage>> response = restConnect.get(PathEnum.GetAllLogs, null, new Page(0, 100).toMap(), token);
        RequestResponse<List<LogMessage>> response = new RestConnect(PathEnum.GetAllLogs, token).create().executeContent(new Page(0, 100).toMap());
        for(LogMessage logMessage : response.getResponse()) {
            logMessage.initializeLogLevel();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    public RequestResponse<List<? extends ILogMessage>> getCustomLogs() {
        // RequestResponse<List<LogMessage>> response = restConnect.get(PathEnum.GetCustomLogs, null, new Page(0, 100).toMap(), token);
        RequestResponse<List<LogMessage>> response = new RestConnect(PathEnum.GetCustomLogs, token).create().executeContent(new Page(0, 100).toMap());
        for(LogMessage logMessage : response.getResponse()) {
            logMessage.initializeLogLevel();
        }
        return new RequestResponse<>(response.getResponse(), response.getConnectionState());
    }

    /*Connection Methods */
    @Override
    public void logout() {
        new RestConnect(PathEnum.Logout,token).create().logout();
        departments.clear();
        currentDepartment = null;
        chats.clear();
        currentChat = null;
        users.clear();
        loginUser = null;
        token = null;
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
        RequestResponse<Login> temp = new RestConnect(PathEnum.Login).create().login(password,username);
        if (temp.getConnectionState() == ConnectionState.SUCCESS) {
            token = temp.getResponse().getAccess_token();
            hubConnect.connect(token);
            // RequestResponse<LoginUser> data = restConnect.get(PathEnum.GetUserInfo, null, null, token);
            RequestResponse<LoginUser> data = new RestConnect(PathEnum.GetUserInfo, token).create().executeNoParameters();
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