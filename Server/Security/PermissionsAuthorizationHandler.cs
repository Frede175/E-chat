using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Server.Context;

namespace Server.Security
{
    public class PermissionsAuthorizationHandler : AuthorizationHandler<PermissionsAuthorizationRequirement>
    {
        public PermissionsAuthorizationHandler()
        {
        }

        protected override Task HandleRequirementAsync(AuthorizationHandlerContext context, PermissionsAuthorizationRequirement requirement)
        {

            if (requirement.Type == PermissionAttributeType.AND)
            {
                foreach (Permission p in requirement.RequiredPermissions)
                {
                    if (!context.User.HasClaim(UserClaimTypes.Permission, p.ToString())) return Task.CompletedTask;
                }

                context.Succeed(requirement);
                return Task.CompletedTask;
            }
            else
            {
                foreach (Permission p in requirement.RequiredPermissions)
                {
                    if (context.User.HasClaim(UserClaimTypes.Permission, p.ToString())) 
                    {
                        context.Succeed(requirement);
                        return Task.CompletedTask;
                    }
                }

                return Task.CompletedTask;
            }
        }
    }
}
