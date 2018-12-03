package Acquaintence;

import Business.Connection.RequestResponse;
import Business.Models.Chat;
import Business.Models.Department;
import Business.Models.User;

import java.util.ArrayList;
import java.util.List;

public interface IBusinessFacade {

    RequestResponse<List<? extends IChat>> getChats();
    RequestResponse<List<? extends IUser>> getUsersInChat();
    IChat getCurrentChat();
    List<? extends IChat> getExistingChats();
    List<? extends IChat> getAvailableChats(String userId);
    List<? extends IChat> getUsersChats(String userId);
    ConnectionState createChat(String chatName, int departmentId);
    ConnectionState addUserToChat(int chatId, String userId);
    void removeUserFromChat(int chatId, String userId);
    void setCurrentChat(int chatId);

    RequestResponse<List<? extends  IUser>> getUsers();
    List<? extends IUser> getExistingUsers();
    void createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds);
    void deleteUser(String userId);

    RequestResponse<List<? extends IDepartment>> getDepartments();
    RequestResponse<List<? extends IDepartment>> getAllDepartments();
    RequestResponse<List<IUser>> getAllUsersInDepartment(int departmentId);
    RequestResponse<List<IDepartment>> getAvailableDepartments(String userId);
    void createDepartment(String departmentname);
    void deleteDepartment(int depId);
    void addUserToDepartment(int depId, String userId);
    void removeUserFromDepartment(String userId, int departmentId);
    void updateDepartment(int depId, String name);

    RequestResponse<List<? extends IMessageIn>> getMessages(int chatId);
    RequestResponse<List<? extends IMessageIn>> getMessages();
    RequestResponse<Chat> createDirectMessage(String name, IUser user);
    void sendMessage(String message);

    RequestResponse<List<? extends IRole>> getRoles();
    RequestResponse<List<String>> getAllPermissions();
    List<String> getRolesPermissions(String roleName);
    void deleteRole(String roleName);
    void createRole(List<String> permissions, String name);
    void addPermissionsToRole(String role, List<String> permissions);
    void removePermissionsFromRole(String role, List<String> permissions);
    void addRoleToUser(String userId, String role);

    ILoginUser getLoginUser();
    ConnectionState login(String username, String password);
    void logout();
    void disconnectHub();
}
