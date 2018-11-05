using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Server.Context;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    public class RoleController : Controller
    {

        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IChatService _chatService;
        private readonly RoleManager<IdentityRole> _roleManager;

        public RoleController(UserManager<ApplicationUser> userManager, IChatService chatService, RoleManager<IdentityRole> roleManager)
        {
            _userManager = userManager;
            _chatService = chatService;
            _roleManager = roleManager;
        }



        // POST: https://localhost:5001/api/Role/add
        [HttpPost]
        [RequiresPermissionAttribute(Permission.CreateUserRole)]
        public async Task<ActionResult> CreateUserRole(string role, List<Permission> permissions)
        {
            var newRole = await _roleManager.FindByNameAsync(role);

            if (newRole == null)
            {
                newRole = new IdentityRole(role);
                var result = await _roleManager.CreateAsync(newRole);
                if (result.Succeeded)
                {
                    foreach(Permission permission in permissions){
                        await _roleManager.AddClaimAsync(newRole, new Claim(UserClaimTypes.Permission, permission.ToString()));
                    }

                    return new OkResult();
                }
            }
            return BadRequest();
        }

        // POST: https://localhost:5001/api/Role/addPermission
        [HttpPost]
        [RequiresPermissionAttribute(Permission.AddPermissionToRole)]
        public async Task<ActionResult> AddPermissionToRole(string permissionName,IdentityRole role)
        {
            string[] permissions = Enum.GetNames(typeof(Permission));

            if (permissions.Contains(permissionName))
            {
            var result = await _roleManager.AddClaimAsync(role, new Claim(UserClaimTypes.Permission, permissionName));

                if (result.Succeeded)
                {
                    return new OkResult();
                }
            }
            return new BadRequestResult();
        }
    }
}

