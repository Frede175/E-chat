package Business.Connection;

import Business.Models.Department;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public enum PathEnum {


    CreateDepartment("/api/department/", ConnectionType.POST, null),
    GetDepartments("/api/department/", ConnectionType.GET, new TypeToken<List<Department>>(){}.getType()),
    CreateUser("/api/Auth", ConnectionType.POST, null);

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