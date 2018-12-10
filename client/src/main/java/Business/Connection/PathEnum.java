package Business.Connection;

import Business.Models.*;
import com.google.gson.reflect.TypeToken;
import org.apache.http.entity.ContentType;

import java.lang.reflect.Type;
import java.util.List;

public enum PathEnum {

    Login("/connect/token/", ConnectionType.POST, ContentType.APPLICATION_FORM_URLENCODED, Login.class),
    Logout("/connect/logout/", ConnectionType.POST, ContentType.APPLICATION_FORM_URLENCODED),
    CreateDepartment("/api/department/", ConnectionType.POST, Department.class),
    UpdateDepartment("/api/department/", ConnectionType.PUT),
    GetDepartmentsForUser("/api/department/user/", ConnectionType.GET, new TypeToken<List<Department>>(){}.getType()),
    CreateUser("/api/user/create/", ConnectionType.POST, User.class),
    DeleteDepartment("/api/department/", ConnectionType.DELETE),
    GetMessages("/api/Messages/", ConnectionType.GET, new TypeToken<List<MessageIn>>() {}.getType()),
    CreateChatroom("/api/chat/", ConnectionType.POST, Chat.class),
    GetAllChats("/api/chat/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    GetChats("/api/chat/user/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    RemoveChatroom("/api/chat/", ConnectionType.DELETE),
    RemoveUserFromChat("/api/chat/remove/", ConnectionType.POST),
    AddUserToChat("/api/chat/add/", ConnectionType.POST),
    LeaveChat("/api/chat/leave/", ConnectionType.POST),
    PutChatRoom("/api/chat/", ConnectionType.PUT),
    GetUserInfo("/api/userinfo/", ConnectionType.GET, LoginUser.class),
    GetUsersInChat("/api/chat/users/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    GetContacts("/api/user/contacts/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    GetAllUsers("/api/user/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    AddUserToDeparment("/api/department/", ConnectionType.POST),
    GetRoles("/api/Role/", ConnectionType.GET, new TypeToken<List<Role>>() {}.getType()),
    CreateDirectMessage("/api/chat/private/", ConnectionType.POST, Chat.class),
    GetDirectMessages("/api/chat/private/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    GetAllDepartments("/api/department/", ConnectionType.GET, new TypeToken<List<Department>>() {}.getType()),
    GetAllPermissions("/api/permission/", ConnectionType.GET, new TypeToken<List<String>>() {}.getType()),
    CreateRole("/api/role/", ConnectionType.POST, Role.class),
    AddRoleToUser("/api/user/role/add/", ConnectionType.PUT),
    RemoveRoleFromUser("/api/User/role/remove/", ConnectionType.PUT),
    GetAvailableRoles("/api/Role/available/", ConnectionType.GET, new TypeToken<List<Role>>() {}.getType()),
    GetAvailableChats("/api/chat/available/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    GetUser("/api/user/", ConnectionType.GET, User.class),
    GetChat("/api/chat/", ConnectionType.GET, Chat.class),
    DeleteRole("/api/role/", ConnectionType.DELETE),
    GetRolesPermissions("/api/role/permission/", ConnectionType.GET, new TypeToken<List<String>>() {}.getType()),
    RemovePermissionsFromRole("/api/role/removeperm/", ConnectionType.POST),
    AddPermissionsToRole("/api/role/addperm/", ConnectionType.POST),
    RemoveUserFromDepartment("/api/department/remove/", ConnectionType.POST),
    GetAllUsersInDepartment("/api/department/users/", ConnectionType.GET, new TypeToken<List<User>>() {}.getType()),
    GetAvailableDepartments("/api/department/available/", ConnectionType.GET, new TypeToken<List<Department>>() {}.getType()),
    DeleteUser("/api/user/delete/", ConnectionType.DELETE),
    GetAllLogs("/api/logging/", ConnectionType.GET, new TypeToken<List<LogMessage>>() {}.getType()),
    GetCustomLogs("/api/logging/custom/", ConnectionType.GET, new TypeToken<List<LogMessage>>() {}.getType());



    PathEnum(String path, ConnectionType type, Type resultType) {
        this(path, type, ContentType.APPLICATION_JSON, resultType);
    }

    PathEnum(String path, ConnectionType type) {
        this(path, type, ContentType.APPLICATION_JSON, null);
    }

    PathEnum(String path, ConnectionType type, ContentType contentType) {
        this(path, type, contentType, null);
    }

    PathEnum(String path, ConnectionType type, ContentType contentType, Type resultType) {
        this.path = path;
        this.type = type;
        this.resultType = resultType;
        this.contentType = contentType;
    }

    private final ContentType contentType;
    private final String path;
    private final ConnectionType type;
    private final Type resultType;

    public String getPath() {
        return path;
    }

    public ConnectionType getType() {
        return type;
    }

    public Type getResultType() {
        return resultType;
    }

    public ContentType getContentType() {
        return contentType;
    }
}