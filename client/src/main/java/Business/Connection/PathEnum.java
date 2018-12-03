package Business.Connection;

import Business.Models.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public enum PathEnum {


    CreateDepartment("/api/department/", ConnectionType.POST, new TypeToken<Department>() {}.getType()),
    UpdateDepartment("/api/department/", ConnectionType.PUT, null),
    GetDepartments("/api/department/user/", ConnectionType.GET, new TypeToken<List<Department>>(){}.getType()),
    CreateUser("/api/user/create/", ConnectionType.POST, new TypeToken<User>() {}.getType()),
    DeleteDepartment("/api/department/", ConnectionType.DELETE, null),
    GetMessages("/api/Messages/", ConnectionType.GET, new TypeToken<List<MessageIn>>() {}.getType()),
    CreateChatroom("/api/chat/", ConnectionType.POST, Chat.class),
    GetChats("/api/chat/user/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    RemoveChatroom("/api/chat/", ConnectionType.DELETE, null),
    RemoveUserFromChat("/api/chat/remove/", ConnectionType.POST, null),
    AddUserToChat("/api/chat/add/", ConnectionType.POST, null),
    LeaveChat("/api/chat/leave/", ConnectionType.POST, null),
    PutChatRoom("/api/chat/", ConnectionType.PUT, null),
    GetUserInfo("/api/userinfo/", ConnectionType.GET, new TypeToken<LoginUser>() {}.getType()),
    GetUsersInChat("/api/chat/users/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    GetUsers("/api/user/contacts/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    AddUserToDeparment("/api/department/", ConnectionType.POST, null),
    GetRoles("/api/Role/", ConnectionType.GET, new TypeToken<List<Role>>() {}.getType()),
    CreateDirectMessage("/api/chat/private/", ConnectionType.POST, new TypeToken<Chat>() {}.getType()),
    GetDirectMessages("/api/chat/private/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    GetAllDepartments("/api/department/", ConnectionType.GET, new TypeToken<List<Department>>() {}.getType()),
    GetAllPermissions("/api/permission/", ConnectionType.GET, new TypeToken<List<String>>() {}.getType()),
    CreateRole("/api/role/", ConnectionType.POST, new TypeToken<Role>() {}.getType()),
    AddRoleToUser("/api/user/", ConnectionType.PUT, null),
    GetAvailableChats("/api/chat/available/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    GetUser("/api/user/", ConnectionType.GET, new TypeToken<User>() {}.getType()),
    GetChat("/api/chat/", ConnectionType.GET, new TypeToken<Chat>() {}.getType()),
    DeleteRole("/api/role/", ConnectionType.DELETE, null),
    GetRolesPermissions("/api/role/permission/", ConnectionType.GET, new TypeToken<List<String>>() {}.getType()),
    RemovePermissionsFromRole("/api/role/removeperm/", ConnectionType.POST, null),
    AddPermissionsToRole("/api/role/addperm/", ConnectionType.POST, null),
    RemoveUserFromDepartment("/api/department/remove/", ConnectionType.POST, null),
    GetAllUsersInDepartment("/api/department/users/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    GetAvailableDepartments("/api/department/available/", ConnectionType.GET, new TypeToken<List<Department>>() {}.getType()),
    DeleteUser("/api/user/delete/", ConnectionType.DELETE, null);




    private PathEnum(String path, ConnectionType type, Type resultType) {
        this.path = path;
        this.type = type;
        this.resultType = resultType;
    }

    private String path;
    private ConnectionType type;
    private Type resultType;

    public String getPath() {
        return path;
    }

    public ConnectionType getType() {
        return type;
    }

    public Type getResultType() {
        return resultType;
    }
}