using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;

namespace Server.Security
{
    public class PermissionsAuthorizationHandler : AuthorizationHandler<PermissionsAuthorizationRequirement>
    {
        public PermissionsAuthorizationHandler()
        {
        }

        protected override Task HandleRequirementAsync(AuthorizationHandlerContext context, PermissionsAuthorizationRequirement requirement)
        {
            if (!requirement.RequiredPermissions.Select(r => r.ToString()).Except(context.User.Claims.Select(c => c.Type)).Any())
            {
                context.Succeed(requirement);
            }
            return Task.CompletedTask;
            
        }
    }
}
