package Acquaintence;

import Business.Connection.RequestResponse;
import Business.Models.Chat;
import Business.Models.Department;
import Business.Models.User;

import java.util.ArrayList;
import java.util.List;

public interface IBusinessFacade {
    ConnectionState login(String username, String password);
    void addUserToDepartment(User user, Department department);
    RequestResponse<List<? extends IChat>> getChats();
    RequestResponse<List<? extends  IUser>> getUsers();
    RequestResponse<Chat> createDirectMessage(String name, IUser user);
    RequestResponse<String> addUserToSpecificChat(String userSub, IChat chat);
    void setCurrentChat(int chatId);
    void sendMessage(String message);
    RequestResponse<List<? extends IMessageIn>> getMessages(int chatId);
    RequestResponse<List<? extends IMessageIn>> getMessages();
    RequestResponse<List<? extends IUser>> getUsersInChat();
    RequestResponse<List<? extends IDepartment>> getDepartments();
    RequestResponse<List<? extends IDepartment>> getAllDepartments();
    IChat getCurrentChat();
    RequestResponse<List<? extends String>> getAllPermissions();
    List<? extends IChat> getExistingChats();
    void deleteUserRole(String roleName);
    ConnectionState createChat(String chatName, int departmentId);
    void createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds);
    void createDepartment(String departmentname);
    void createUserRole(List<String> permissions, String name);
    ILoginUser getLoginUser();
    RequestResponse<List<? extends IRole>> getRoles();
    List<? extends IUser> getExistingUsers();
    void addRoleToUser(String userId, String role);

    void logout();
}
