package Acquaintence;

import Business.Connection.RequestResponse;
import Business.Models.Chat;
import Business.Models.Department;
import Business.Models.LogMessage;
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
    void deleteChat(int chatId);
    ConnectionState addUserToChat(int chatId, String userId);
    void removeUserFromChat(int chatId, String userId);
    void setCurrentChat(int chatId);
    void leaveChat(int chatId);

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

    RequestResponse<List<? extends IMessageIn>> getMessages(int chatId, int page);
    RequestResponse<List<? extends IMessageIn>> getMessages(int page);
    void createDirectMessage(String name, String userId);
    void sendMessage(String message);

    RequestResponse<List<? extends IRole>> getRoles();
    RequestResponse<List<String>> getAllPermissions();
    List<String> getRolesPermissions(String roleName);
    void deleteRole(String roleName);
    void createRole(List<String> permissions, String name);
    void addPermissionsToRole(String role, List<String> permissions);
    void removePermissionsFromRole(String role, List<String> permissions);
    void addRoleToUser(String userId, String role);

    RequestResponse<List<? extends ILogMessage>> getAllLogs(int page);
    RequestResponse<List<? extends ILogMessage>> getCustomLogs(int page);

    ILoginUser getLoginUser();
    ConnectionState login(String username, String password);
    void logout();
    void disconnectHub();
}
