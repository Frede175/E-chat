package Business.Connection;


public enum PermissionEnum {


    //DepartmentController

    GetUserDepartments("GetUserDepartments"),
    GetDepartments("GetDepartments"),
    CreateDepartment("CreateDepartment"),
    AddUserToDepartment("AddUserToDepartment"),
    RemoveUserFromDepartment("RemoveUserFromDepartment"),
    DeleteDepartment("DeleteDepartment"),
    UpdateDepartment("UpdateDepartment"),


    //ChatController

    GetChats("GetChats"),
    CreateChat("CreateChat"),
    LeaveChat("LeaveChat"),
    AddUserToChat("AddUserToChat"),
    RemoveUserFromChat("RemoveUserFromChat"),
    GetUsersInChat("GetUsersInChat"),


    //UserController

    GetUsers("GetUsers"),
    GetContacts("GetContacts"),
    CreateUser("CreateUser"),
    DeleteUser("DeleteUser"),
    AddAdditionalRole("AddAdditionalRole"),


    //RoleController

    CreateUserRole("CreateUserRole"),
    AddPermissionToRole("AddPermissionToRole"),
    RemovePermissionFromRole("RemovePermissionFromRole"),
    DeleteRole("DeleteRole");

    private PermissionEnum(String permission) {
        this.permission = permission;
    }

    private String permission;

    public String getPermission() {
        return permission;
    }
}
