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
using Microsoft.Extensions.Logging;
using Server.Context;
using Server.Logging;
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

        private readonly ILogger<RoleController> _logger;

        public RoleController(UserManager<ApplicationUser> userManager, IChatService chatService, RoleManager<IdentityRole> roleManager, ILogger<RoleController> logger)
        {
            _userManager = userManager;
            _chatService = chatService;
            _roleManager = roleManager;
            _logger = logger;
        }

        // GET: https://localhost:5001/api/Role/{roleId}
        [HttpGet("{roleId}")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateRole, Permission.DeleteRole, Permission.AddPermissionToRole, Permission.RemovePermissionFromRole, Permission.CreateUser)]
        public async Task<ActionResult<Role>> GetRole(string roleId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.GetItem, "{username} getting role ({id}).", username, roleId);

            var role = await _roleManager.FindByIdAsync(roleId);
            if (role != null)
            {
                return Ok(new Role(role));
            }

            _logger.LogWarning(LoggingEvents.GetItemNotFound, "{username} getting role ({id}), NOT FOUND.", username, roleId);
            return NotFound();
        }

        // GET: https://localhost:5001/api/Role/permission/{name}
        [HttpGet("permission/{roleId}")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateRole, Permission.DeleteRole, Permission.AddPermissionToRole, Permission.RemovePermissionFromRole, Permission.CreateUser)]
        public async Task<ActionResult<IEnumerable<string>>> GetRolePermissions(string roleId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting role ({id}) permissions.", username, roleId);

            var role = await _roleManager.FindByIdAsync(roleId);
            if (role != null)
            {
                var perms = (await _roleManager.GetClaimsAsync(role)).Where(c => c.Type == UserClaimTypes.Permission).Select(c => c.Value);
                return Ok(perms);
            }

            _logger.LogWarning(LoggingEvents.ListItemsNotFound, "{username} getting role ({id}) permissions, NOT FOUND", username, roleId);
            return NotFound();
        }


        // GET: https://localhost:5001/api/Role/
        [HttpGet]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateRole, Permission.DeleteRole, Permission.AddPermissionToRole, Permission.RemovePermissionFromRole, Permission.CreateUser)]
        public async Task<ActionResult<List<Role>>> GetRoles() 
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting roles.", username);

            return await _roleManager.Roles.Select(r => new Role(r)).ToListAsync();
        }



        // POST: https://localhost:5001/api/Role/{name}
        [HttpPost("{name}")]
        [RequiresPermissionAttribute(permissions: Permission.CreateRole)]
        public async Task<ActionResult> CreateUserRole(string name, [FromBody] List<string> addedPermissions)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} inserting role ({name}).", username, name);

            var newRole = await _roleManager.FindByNameAsync(name);
            string[] permissions = Enum.GetNames(typeof(Permission));

            if (newRole == null)
            {
                newRole = new IdentityRole(name);
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
                _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed inserting role ({name})", username, name);
                return BadRequest();
            }
            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} inserting role ({name}), ALLREADY EXIST", username, name);
            return BadRequest($"Role with name {name} allready exist.");
        }

        // POST: https://localhost:5001/api/Role/addperm/{name}
        [HttpPost("addperm/{roleId}")]
        [RequiresPermissionAttribute(permissions: Permission.AddPermissionToRole)]
        public async Task<ActionResult> AddPermissionToRole(string roleId, [FromBody] List<string> permissionNames)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.UpdateRelativeItem, "{username} adding permissions to role ({role}).", username, roleId);


            var role = await _roleManager.FindByIdAsync(roleId);

            if (role == null)
            {
                _logger.LogWarning(LoggingEvents.UpdateRelativeItemNotFound, "{username} adding permissions to role ({role}), NOT FOUND", username, roleId);
                return NotFound();
            }


            var permissions = Enum.GetNames(typeof(Permission));

            if (permissions.Intersect(permissionNames).Count() == permissionNames.Count())
            {
                var claims = await _roleManager.GetClaimsAsync(role);
                if (claims.Select(c => c.Value).Intersect(permissionNames).Count() == 0) {
                    foreach (var permission in permissionNames) {
                        await _roleManager.AddClaimAsync(role, new Claim(UserClaimTypes.Permission, permission));
                    }

                    return NoContent();
                }

                _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} adding ermissions to role ({role}), permission allready on role.", username, role);
                return BadRequest("Permission allready on role.");
            }
            _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} adding permissions to role ({role}), unrecognized permission.", username, role);
            return BadRequest("Unrecognized permission.");
        }

        // POST: https://localhost:5001/api/Role/removeperm/{name}
        [HttpPost("removeperm/{roleId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemovePermissionFromRole)]
        public async Task<ActionResult> RemovePermissionFromRole(string roleId, [FromBody] List<string> permissionNames)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.UpdateRelativeItem, "{username} removing permissions to role ({role}).", username, roleId);

            var permissions = Enum.GetNames(typeof(Permission));
            var role = await _roleManager.FindByIdAsync(roleId);

            if (role == null)
            {
                _logger.LogWarning(LoggingEvents.UpdateRelativeItemNotFound, "{username} removing permissions to role ({role}), NOT FOUND", username, roleId);
                return NotFound();
            }

            if (permissions.Intersect(permissionNames).Count() == permissionNames.Count() && role != null)
            {
                var claims = await _roleManager.GetClaimsAsync(role);
                if (claims.Select(c => c.Value).Intersect(permissionNames).Count() == permissionNames.Count()) 
                {
                    foreach (var permission in permissionNames) 
                    {
                        await _roleManager.RemoveClaimAsync(role, new Claim(UserClaimTypes.Permission, permission));
                    }
                    return NoContent();
                }

                _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} removing ermissions to role ({role}), permission not on role.", username, role);
                return BadRequest("Permission not on role.");
            }

            _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} removing permissions to role ({role}), unrecognized permission.", username, role);
            return BadRequest("Unrecognized permission.");
        }

        // DELETE: https://localhost:5001/api/Role/delete/{role}
        [HttpDelete("{role}")]
        [RequiresPermissionAttribute(permissions: Permission.DeleteRole)]
        public async Task<ActionResult> DeleteRole(string roleId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} deleting role ({role}).", username, roleId);

            var thisRole = await _roleManager.FindByIdAsync(roleId);

            if (thisRole != null)
            {
                var result = await _roleManager.DeleteAsync(thisRole);

                if (result.Succeeded)
                {
                    return NoContent();
                }

                _logger.LogWarning(LoggingEvents.DeleteItemFail, "{username} failed deleting role ({role}).", username, roleId);
                return BadRequest();
            }

            _logger.LogWarning(LoggingEvents.DeleteItemNotFound, "{username} deleting role ({role}), NOT FOUND.", username, roleId);
            return NotFound();
        }

    }
}

