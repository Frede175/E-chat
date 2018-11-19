using System;
namespace Server.Security
{

    public enum Permission
    {

        //DepartmentController

        // GetUserDepartments,
        //GetAllDepartments, -- CreateDepartment, AddUserToDepartment, RemoveUserFromDepartment, DeleteDepartment, UpdateDepartment
        CreateDepartment,
        AddUserToDepartment,
        RemoveUserFromDepartment,
        DeleteDepartment,
        UpdateDepartment,


        //ChatController

        //GetAllChats, -- CreateChat, AddUserToChat, RemoveUserFromChat
        CreateChat,
        // CreatePrivateChat,
        LeaveChat,
        AddUserToChat,
        RemoveUserFromChat,
        // GetUsersInChat,


        //UserController

        //GetUsers, -- CreateUser, DeleteUser, AddAdditionalRole, ...
        // GetContacts,
        CreateUser,
        DeleteUser,
        AddAdditionalRole,


        //RoleController
        //GetRoles, -- CreateUserRole, AddPermissionToRole, RemovePermissionFromRole, DeleteRole
        CreateUserRole,
        AddPermissionToRole,
        RemovePermissionFromRole,
        DeleteRole,


        //BasicRole
        BasicPermissions
    }
}
