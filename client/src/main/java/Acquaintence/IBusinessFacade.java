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
    RequestResponse<List<? extends IChat>> getAllChats();
    IChat getCurrentChat();
    List<? extends IChat> getExistingChats();
    List<? extends IChat> getAvailableChats(String userId);
    List<? extends IChat> getUsersChats(String userId);
    ConnectionState createChat(String chatName, int departmentId);
    ConnectionState deleteChat(int chatId);
    ConnectionState addUserToChat(int chatId, String userId);
    ConnectionState removeUserFromChat(int chatId, String userId);
    void setCurrentChat(int chatId);
    void leaveChat(int chatId);

    RequestResponse<List<? extends  IUser>> getUsers();
    RequestResponse<List<? extends  IUser>> getAllUsers();
    List<? extends IUser> getExistingUsers();
    ConnectionState createUser(String username, String password, IRole role, ArrayList<Integer> departmentsIds);
    ConnectionState deleteUser(String userId);

    RequestResponse<List<? extends IDepartment>> getDepartments();
    RequestResponse<List<? extends IDepartment>> getAllDepartments();
    RequestResponse<List<IUser>> getAllUsersInDepartment(int departmentId);
    RequestResponse<List<IDepartment>> getAvailableDepartments(String userId);
    ConnectionState createDepartment(String departmentname);
    ConnectionState deleteDepartment(int depId);
    ConnectionState addUserToDepartment(int depId, String userId);
    ConnectionState removeUserFromDepartment(String userId, int departmentId);
    ConnectionState updateDepartment(int depId, String name);

    RequestResponse<List<? extends IMessageIn>> getMessages(int chatId, int page);
    RequestResponse<List<? extends IMessageIn>> getMessages(int page);
    void createDirectMessage(String name, String userId);
    void sendMessage(String message);

    RequestResponse<List<? extends IRole>> getRoles();
    RequestResponse<List<IRole>> getAvailableRoles(String userId);
    RequestResponse<List<String>> getAllPermissions();
    List<String> getRolesPermissions(String roleName);

    ConnectionState deleteRole(String roleId);
    ConnectionState createRole(List<String> permissions, String name);
    ConnectionState addPermissionsToRole(String role, List<String> permissions);
    ConnectionState removePermissionsFromRole(String role, List<String> permissions);
    ConnectionState addRoleToUser(String userId, String role);
    ConnectionState removeRoleFromUser(String userId, String roleId);


    RequestResponse<List<? extends ILogMessage>> getAllLogs(int page);
    RequestResponse<List<? extends ILogMessage>> getCustomLogs(int page);

    ILoginUser getLoginUser();
    ConnectionState login(String username, String password);
    void logout();
    void disconnectHub();
}
