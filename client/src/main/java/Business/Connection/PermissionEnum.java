package Business.Connection;


public enum PermissionEnum {

    // DepartmentController

    // TODO REMOVE GetAllDepartments
    CreateDepartment(PermissionType.DEPARTMENT),
    AddUserToDepartment(PermissionType.DEPARTMENT),
    RemoveUserFromDepartment(PermissionType.DEPARTMENT),
    DeleteDepartment(PermissionType.DEPARTMENT),
    UpdateDepartment(PermissionType.DEPARTMENT),
    GetAllDepartments(PermissionType.DEPARTMENT),


    // ChatController
    CreateChat(PermissionType.CHAT),
    LeaveChat(PermissionType.CHAT),
    AddUserToChat(PermissionType.CHAT),
    RemoveUserFromChat(PermissionType.CHAT),
    GetAllChats(PermissionType.CHAT),


    // UserController
    // TODO REMOVE GetUsers
    GetUsers(PermissionType.USER),
    CreateUser(PermissionType.USER),
    DeleteUser(PermissionType.USER),
    AddAdditionalRole(PermissionType.USER),


    // RoleController

    CreateUserRole(PermissionType.ROLE),
    AddPermissionToRole(PermissionType.ROLE),
    RemovePermissionFromRole(PermissionType.ROLE),
    DeleteRole(PermissionType.ROLE),

    // Non-admin
    BasicPermissions(null);



    private PermissionEnum(PermissionType type) {
        this.type = type;
    }
    private PermissionType type;

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
