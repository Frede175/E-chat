using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
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

        // GET: https://localhost:5001/api/Role/{roleId}
        [HttpGet("{roleId}")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateUserRole, Permission.DeleteRole, Permission.AddPermissionToRole, Permission.RemovePermissionFromRole, Permission.CreateUser)]
        public async Task<ActionResult<Role>> GetRole(string roleId) 
        {
            var role = await _roleManager.FindByIdAsync(roleId);
            if (role != null)
            {
                return Ok(new Role(role));
            } 
            return NotFound();
        }


        // GET: https://localhost:5001/api/Role/
        [HttpGet]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateUserRole, Permission.DeleteRole, Permission.AddPermissionToRole, Permission.RemovePermissionFromRole, Permission.CreateUser)]
        public async Task<ActionResult<List<Role>>> GetRoles() 
        {
            return await _roleManager.Roles.Select(r => new Role(r)).ToListAsync();
        }



        // POST: https://localhost:5001/api/Role/{role}
        [HttpPost("{role}")]
        [RequiresPermissionAttribute(permissions: Permission.CreateUserRole)]
        public async Task<ActionResult> CreateUserRole(string role, List<string> addedPermissions)
        {
            var newRole = await _roleManager.FindByNameAsync(role);
            string[] permissions = Enum.GetNames(typeof(Permission));


            if (newRole == null)
            {
                newRole = new IdentityRole(role);
                var result = await _roleManager.CreateAsync(newRole);
                if (result.Succeeded)
                {
                    foreach(string permission in addedPermissions)
                    {
                        if (permissions.Contains(permission))
                        {
                            await _roleManager.AddClaimAsync(newRole, new Claim(UserClaimTypes.Permission, permission));
                        }
                    }

                    return CreatedAtAction(nameof(GetRole), new { roleId = newRole.Id }, new Role(newRole));
                }
            }
            return BadRequest();
        }

        // POST: https://localhost:5001/api/Role/addperm/{role}
        [HttpPost("addperm/{role}")]
        [RequiresPermissionAttribute(permissions: Permission.AddPermissionToRole)]
        public async Task<ActionResult> AddPermissionToRole(string role, [FromBody] List<string> permissionNames)
        {
            var permissions = Enum.GetNames(typeof(Permission));
            var userRole = await _roleManager.FindByNameAsync(role);

            if (permissions.Intersect(permissionNames).Count() == permissionNames.Count() && userRole != null)
            {
                var claims = await _roleManager.GetClaimsAsync(userRole);
                if (claims.Select(c => c.Value).Intersect(permissionNames).Count() == 0) {
                    foreach (var permission in permissionNames) {
                        await _roleManager.AddClaimAsync(userRole, new Claim(UserClaimTypes.Permission, permission));
                    }

                    return Ok();
                }
            }
            return BadRequest();
        }

        // POST: https://localhost:5001/api/Role/removeperm/{role}
        [HttpPost("removeperm/{role}")]
        [RequiresPermissionAttribute(permissions: Permission.RemovePermissionFromRole)]
        public async Task<ActionResult> RemovePermissionFromRole(string role, [FromBody] List<string> permissionNames)
        {
            var permissions = Enum.GetNames(typeof(Permission));
            var userRole = await _roleManager.FindByNameAsync(role);

            if (permissions.Intersect(permissionNames).Count() == permissionNames.Count() && userRole != null)
            {
                var claims = await _roleManager.GetClaimsAsync(userRole);
                if (claims.Select(c => c.Value).Intersect(permissionNames).Count() == permissionNames.Count()) 
                {
                    foreach (var permission in permissionNames) 
                    {
                        await _roleManager.RemoveClaimAsync(userRole, new Claim(UserClaimTypes.Permission, permission));
                    }
                    return Ok();
                }
            }
            return BadRequest();
        }

        // DELETE: https://localhost:5001/api/Role/delete/{role}
        [HttpDelete("{role}")]
        [RequiresPermissionAttribute(permissions: Permission.DeleteRole)]
        public async Task<ActionResult> DeleteRole(string role)
        {
            var thisRole = await _roleManager.FindByNameAsync(role);

            if (thisRole != null)
            {
                var result = await _roleManager.DeleteAsync(thisRole);

                if (result.Succeeded)
                {
                    return new OkResult();
                }
            }
            return new BadRequestResult();
        }

    }
}

