using System;
namespace Server.Security
{

    public enum Permission
    {

        //DepartmentController

        GetUserDepartments,
        GetDepartments,
        CreateDepartment,
        AddUserToDepartment,
        RemoveUserFromDepartment,
        DeleteDepartment,
        UpdateDepartment,


        //ChatController

        GetChats,
        CreateChat,
        LeaveChat,
        AddUserToChat,
        RemoveUserFromChat,


        //UserController

        GetUsers,
        GetContacts,
        CreateUser,
        DeleteUser,
        AddAdditionalRole,


        //RoleController

        CreateUserRole,
        AddPermissionToRole
    }
}
