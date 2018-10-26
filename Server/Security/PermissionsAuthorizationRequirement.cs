using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Authorization;

namespace Server.Security
{
    public class PermissionsAuthorizationRequirement : IAuthorizationRequirement
    {
       
        public IEnumerable<Permission> RequiredPermissions { get; }

        public PermissionsAuthorizationRequirement(IEnumerable<Permission> requiredPermissions)
        {
            RequiredPermissions = requiredPermissions;
        }
    }
}
