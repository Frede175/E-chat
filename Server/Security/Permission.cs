﻿using System;
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
        RemoveChat,
        AddUserToChat,
        RemoveUserFromChat,
        // GetUsersInChat,


        //UserController

        //GetUsers, -- CreateUser, DeleteUser, AddAdditionalRole, ...
        // GetContacts,
        CreateUser,
        DeleteUser,
        AddRoleToUser,
        RemoveRoleFromUser,


        //RoleController
        //GetRoles, -- CreateUserRole, AddPermissionToRole, RemovePermissionFromRole, DeleteRole
        CreateRole,
        AddPermissionToRole,
        RemovePermissionFromRole,
        DeleteRole,


        //BasicRole
        BasicPermissions,

        //Logs
        SeeLogs,
        SeeAllLogs
        
    }
}
