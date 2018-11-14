package Business.Connection;


public enum PermissionEnum {


    //TODO remove strings in enum
    //DepartmentController

    GetUserDepartments("GetUserDepartments", PermissionType.DEPARTMENT),
    GetDepartments("GetDepartments", PermissionType.DEPARTMENT),
    CreateDepartment("CreateDepartment", PermissionType.DEPARTMENT),
    AddUserToDepartment("AddUserToDepartment", PermissionType.DEPARTMENT),
    RemoveUserFromDepartment("RemoveUserFromDepartment", PermissionType.DEPARTMENT),
    DeleteDepartment("DeleteDepartment", PermissionType.DEPARTMENT),
    UpdateDepartment("UpdateDepartment", PermissionType.DEPARTMENT),


    //ChatController

    GetChats("GetAllChats", PermissionType.CHAT),
    CreateChat("CreateChat", PermissionType.CHAT),
    LeaveChat("LeaveChat", PermissionType.CHAT),
    AddUserToChat("AddUserToChat", PermissionType.CHAT),
    RemoveUserFromChat("RemoveUserFromChat", PermissionType.CHAT),
    GetUsersInChat("GetUsersInChat", PermissionType.CHAT),


    //UserController

    GetUsers("GetUsers", PermissionType.USER),
    GetContacts("GetContacts", PermissionType.USER),
    CreateUser("CreateUser", PermissionType.USER),
    DeleteUser("DeleteUser", PermissionType.USER),
    AddAdditionalRole("AddAdditionalRole", PermissionType.USER),


    //RoleController

    CreateUserRole("CreateUserRole", PermissionType.ROLE),
    AddPermissionToRole("AddPermissionToRole", PermissionType.ROLE),
    RemovePermissionFromRole("RemovePermissionFromRole", PermissionType.ROLE),
    DeleteRole("DeleteRole", PermissionType.ROLE);



    private PermissionEnum(String permission, PermissionType type) {
        this.permission = permission;
        this.type = type;
    }

    private String permission;
    private PermissionType type;

    public String getPermission() {
        return permission;
    }
    public PermissionType getType() {
        return type;
    }

    public boolean hasElevatedPermission() {
        if(type != null) {
            return true;
        }
        return false;
    }
}
