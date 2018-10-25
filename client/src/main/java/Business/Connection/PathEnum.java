package Business.Connection;

import Business.Models.Chat;
import Business.Models.Department;
import Business.Models.Message;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public enum PathEnum {


    CreateDepartment("/api/department/", ConnectionType.POST, null),
    GetDepartments("/api/department/", ConnectionType.GET, new TypeToken<List<Department>>(){}.getType()),
    CreateUser("/api/Auth", ConnectionType.POST, null),
    DeleteDepartment("/api/department/", ConnectionType.DELETE, null),
    GetMessages("/api/messages/", ConnectionType.GET, new TypeToken<List<Message>>() {}.getType()),
    DeleteUser("/api/departments/", ConnectionType.DELETE, null),
    CreateChatroom("/api/chat/", ConnectionType.POST, null),
    GetChats("/api/chat/", ConnectionType.GET, new TypeToken<List<Chat>>() {}.getType()),
    DeleteChatroom("/api/chat/", ConnectionType.DELETE, null),
    RemoveUserFromChat("/api/chat/remove/", ConnectionType.POST, null),
    AddUserToChat("/api/chat/add/", ConnectionType.POST, null),
    LeaveChat("/api/chat/leave/", ConnectionType.POST, null);

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