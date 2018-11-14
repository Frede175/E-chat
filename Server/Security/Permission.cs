using System;
namespace Server.Security
{

    public enum Permission
    {

        //DepartmentController

        // GetUserDepartments,
        GetAllDepartments,
        CreateDepartment,
        AddUserToDepartment,
        RemoveUserFromDepartment,
        DeleteDepartment,
        UpdateDepartment,


        //ChatController

        GetAllChats,
        CreateChat,
        // CreatePrivateChat,
        LeaveChat,
        AddUserToChat,
        RemoveUserFromChat,
        // GetUsersInChat,


        //UserController

        GetUsers,
        // GetContacts,
        CreateUser,
        DeleteUser,
        AddAdditionalRole,


        //RoleController

        CreateUserRole,
        AddPermissionToRole,
        RemovePermissionFromRole,
        DeleteRole,


        //BasicRole
        BasicPermissions
    }
}
