package Business.Connection;


public enum PermissionEnum {

    // DepartmentController
    CreateDepartment(PermissionType.DEPARTMENT),
    AddUserToDepartment(PermissionType.DEPARTMENT),
    RemoveUserFromDepartment(PermissionType.DEPARTMENT),
    DeleteDepartment(PermissionType.DEPARTMENT),
    UpdateDepartment(PermissionType.DEPARTMENT),


    // ChatController
    CreateChat(PermissionType.CHAT),
    //TODO Could be basic permission
    RemoveChat(PermissionType.CHAT),
    AddUserToChat(PermissionType.CHAT),
    RemoveUserFromChat(PermissionType.CHAT),


    // UserController
    CreateUser(PermissionType.USER),
    DeleteUser(PermissionType.USER),
    AddAdditionalRole(PermissionType.USER),


    // RoleController
    CreateRole(PermissionType.ROLE),
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
