using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Authorization;

namespace Server.Security
{
    public class PermissionsAuthorizationRequirement : IAuthorizationRequirement
    {
       
        public IEnumerable<Permission> RequiredPermissions { get; }

        public PermissionAttributeType Type {get;set;}

        public PermissionsAuthorizationRequirement(PermissionAttributeType type, params Permission[] requiredPermissions)
        {
            Type = type;
            RequiredPermissions = requiredPermissions;
        }
    }
}
