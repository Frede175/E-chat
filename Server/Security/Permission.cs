﻿using System;
namespace Server.Security
{

    public enum Permission
    {
        GetUserDepartments,
        GetDepartments,
        CreateDepartment,
        AddUserToDepartment,
        RemoveUserFromDepartment,
        DeleteDepartment,
        UpdateDepartment,
//        CreateUser,
//        DeleteUser
        GetChats,
        CreateChat,
        LeaveChat,
        AddUserToChat,
        RemoveUserFromChat

    }
}
