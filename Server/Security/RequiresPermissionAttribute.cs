using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Logging;

namespace Server.Security
{
    public class RequiresPermissionAttribute : TypeFilterAttribute
    {
        public RequiresPermissionAttribute(params Permission[] permissions)
      : base(typeof(RequiresPermissionAttributeImpl))
        {
            Arguments = new[] { new PermissionsAuthorizationRequirement(permissions) };
        }

        private class RequiresPermissionAttributeImpl : Attribute, IAsyncResourceFilter
        {
            private readonly ILogger _logger;
            private readonly IAuthorizationService _authService;
            private readonly PermissionsAuthorizationRequirement _requiredPermissions;

            public RequiresPermissionAttributeImpl(ILogger<RequiresPermissionAttribute> logger,
                                            IAuthorizationService authService,
                                            PermissionsAuthorizationRequirement requiredPermissions)
            {
                _logger = logger;
                _authService = authService;
                _requiredPermissions = requiredPermissions;
            }


            public async Task OnResourceExecutionAsync(ResourceExecutingContext context, ResourceExecutionDelegate next)
            {

                var variable = await _authService.AuthorizeAsync(context.HttpContext.User,
                                                            context.ActionDescriptor.ToString(),
                                                                 _requiredPermissions);
                if (!variable.Succeeded)
                {
                    context.Result = new ChallengeResult();
                    return;
                }

                await next();
            }
        }
    }
}