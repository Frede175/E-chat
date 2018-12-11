using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AspNet.Security.OpenIdConnect.Primitives;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json.Linq;
using OpenIddict.Abstractions;
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

    public class UserController : Controller
    {
        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IDepartmentService _departmentService;

        private readonly RoleManager<IdentityRole> _roleManager;

        private readonly ILogger<UserController> _logger;

        public UserController(IDepartmentService departmentService,
                               UserManager<ApplicationUser> userManager,
                               IChatService chatService,
                               RoleManager<IdentityRole> roleManager,
                               ILogger<UserController> logger)
        {
            _departmentService = departmentService;
            _userManager = userManager;
            _chatService = chatService;
            _roleManager = roleManager;
            _logger = logger;
        }


        // GET: https://localhost:5001/api/User/{userId}
        [HttpGet("{userId}", Name = "GetUser"), Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, 
            Permission.CreateUser, 
            Permission.DeleteUser, 
            Permission.AddRoleToUser, 
            Permission.RemoveRoleFromUser,
            Permission.AddUserToDepartment, 
            Permission.RemoveUserFromDepartment,
            Permission.AddUserToChat,
            Permission.RemoveUserFromChat
        )]
        public async Task<ActionResult<User>> GetUser(string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.GetItem, "{username} getting user ({id}).", username, userId); 

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null)
            {
                _logger.LogInformation(LoggingEvents.GetItem, "{username} getting user ({id}), NOT FOUND.", username, userId); 
                return NotFound();
            }

            return Ok(new User(user));
        }


        // GET: https://localhost:5001/api/User/ 
        [HttpGet, Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, 
            Permission.CreateUser, 
            Permission.DeleteUser, 
            Permission.AddRoleToUser, 
            Permission.RemoveRoleFromUser,
            Permission.AddUserToDepartment, 
            Permission.RemoveUserFromDepartment,
            Permission.AddUserToChat,
            Permission.RemoveUserFromChat
        )]
        public async Task<ActionResult<ICollection<User>>> GetUsers()
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting users.", username);

            return await _userManager.Users.Select(u => new User(u)).ToListAsync();
        }


        // GET: https://localhost:5001/api/User/contacts
        [HttpGet("contacts"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult<ICollection<User>>> GetContacts()
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting contracts.", username);

            var deps = await _departmentService.GetDepartmentsAsync(_userManager.GetUserId(HttpContext.User)) ;

            var users = await _departmentService.GetUsersInDepartmentsAsync(deps.Select(d => d.Id).ToArray());

            return users.Select(a => new User(a)).ToList();
        }

        // POST: https://localhost:5001/api/User/create
        [HttpPost("create")]
        [RequiresPermissionAttribute(permissions: Permission.CreateUser)]
        public async Task<ActionResult> CreateUser(CreateUser model)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} creating user {other}", username, model.UserName);

            var role = await _roleManager.FindByNameAsync(model.Role);

            if (role == null) 
            {
                _logger.LogWarning(LoggingEvents.InsertItemNotFound, "{username} creating user {other}, NOT FOUND ROLE ({role}).", username, model.UserName, model.Role);
                return NotFound("Role");
            }

            var user = await _userManager.FindByNameAsync(model.UserName);

            if (user != null)
            {
                _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} create user {other}, failed user allready exist.", username, model.UserName);
                return BadRequest("User allready exist.");
            }

            var newUser = new ApplicationUser()
            {
                UserName = model.UserName
            };

            var result = await _userManager.CreateAsync(newUser, model.Password);

            if (result.Succeeded)
            {
                await _userManager.AddToRoleAsync(newUser, model.Role);
                foreach (var department in model.DepartmentIds) {
                    await _departmentService.AddUsersToDepartmentAsync(department, newUser);
                }

                return CreatedAtAction(nameof(GetUser), new { userId = newUser.Id }, new User(newUser));
            }
            
            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to create user {other}.", username, model.UserName);
            return BadRequest();
        }


        // DELETE: https://localhost:5001/api/User/delete
        [HttpDelete("delete/{userId}")]
        [RequiresPermissionAttribute(permissions: Permission.DeleteUser)]
        public async Task<ActionResult> DeleteUser(string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} deleting user ({id}).", username, userId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null) 
            {
                _logger.LogWarning(LoggingEvents.DeleteItemNotFound, "{username} deleting user ({id}), NOT FOUND.", username, userId);
                return NotFound();
            }

            //Removing private chats:
            var privateChats = await _chatService.GetPrivateChatsAsync(userId);
            var removed = await _chatService.RemoveChatsAsync(privateChats.ToArray());

            if (!removed)  {
                return BadRequest();
            }


            var result = await _userManager.DeleteAsync(user);

            if (result.Succeeded)
            {
                return NoContent();
            }

            _logger.LogWarning(LoggingEvents.DeleteItemFail, "{username} failed deleting user ({id}).", username, userId);
            return BadRequest();
        }


        // PUT https://localhost:5001/api/User/role/add/{userId}
        [HttpPut("role/add/{userId}")]
        [RequiresPermissionAttribute(permissions: Permission.AddRoleToUser)]
        public async Task<ActionResult> AddRole(string userId, [FromBody] string roleId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.UpdateRelativeItem, "{username} adding role ({role}) to user ({id}).", username, roleId, userId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null)
            {
                _logger.LogInformation(LoggingEvents.UpdateRelativeItemNotFound, "{username} adding role ({role}) to user ({id}), USER NOT FOUND.", username, roleId, userId);
                return NotFound();
            }

            var role = await _roleManager.FindByIdAsync(roleId);

            if (role == null)
            {
                _logger.LogInformation(LoggingEvents.UpdateRelativeItemNotFound, "{username} adding role ({role}) to user ({id}), ROLE NOT FOUND.", username, roleId, userId);
                return NotFound();
            }

            var result = await _userManager.AddToRoleAsync(user, role.Name);

            if (result.Succeeded)
            {
                return NoContent();
            }

            _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} failed adding role ({role}) to user ({id}).", username, role, userId);
            return BadRequest();
        }

        // PUT https://localhost:5001/api/User/role/remove/{userId}
        [HttpPut("role/remove/{userId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveRoleFromUser)]
        public async Task<ActionResult> RemoveRole(string userId, [FromBody] string roleId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.UpdateRelativeItem, "{username} removing role ({role}) from user ({id}).", username, roleId, userId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null)
            {
                _logger.LogInformation(LoggingEvents.UpdateRelativeItemNotFound, "{username} removing role ({role}) from user ({id}), USER NOT FOUND.", username, roleId, userId);
                return NotFound();
            }

            var role = await _roleManager.FindByIdAsync(roleId);

            if (role == null)
            {
                _logger.LogInformation(LoggingEvents.UpdateRelativeItemNotFound, "{username} removing role ({role}) from user ({id}), ROLE NOT FOUND.", username, roleId, userId);
                return NotFound();
            }

            var result = await _userManager.RemoveFromRoleAsync(user, role.Name);

            if (result.Succeeded)
            {
                return NoContent();
            }

            _logger.LogWarning(LoggingEvents.UpdateRelativeItemFail, "{username} failed removing role ({role}) from user ({id}).", username, role, userId);
            return BadRequest();
        }

        [Authorize]
        [HttpGet("~/api/userinfo"), Produces("application/json")]
        public async Task<IActionResult> Userinfo()
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting userinfo.", username);
            var user = await _userManager.GetUserAsync(User);
            if (user == null)
            {
                _logger.LogWarning(LoggingEvents.ListItems, "{username} getting userinfo, NOT FOUND.", username);
                return BadRequest(new OpenIdConnectResponse
                {
                    Error = OpenIdConnectConstants.Errors.InvalidGrant,
                    ErrorDescription = "The user profile is no longer available."
                });
            }

            var claims = new JObject();

            // Note: the "sub" claim is a mandatory claim and must be included in the JSON response.
            claims[OpenIdConnectConstants.Claims.Subject] = await _userManager.GetUserIdAsync(user);

            if (User.HasClaim(OpenIdConnectConstants.Claims.Scope, OpenIdConnectConstants.Scopes.Email))
            {
                claims[OpenIdConnectConstants.Claims.Email] = await _userManager.GetEmailAsync(user);
                claims[OpenIdConnectConstants.Claims.EmailVerified] = await _userManager.IsEmailConfirmedAsync(user);
            }

            if (User.HasClaim(OpenIdConnectConstants.Claims.Scope, OpenIdConnectConstants.Scopes.Phone))
            {
                claims[OpenIdConnectConstants.Claims.PhoneNumber] = await _userManager.GetPhoneNumberAsync(user);
                claims[OpenIdConnectConstants.Claims.PhoneNumberVerified] = await _userManager.IsPhoneNumberConfirmedAsync(user);
            }

            //if (User.HasClaim(OpenIdConnectConstants.Claims.Scope, OpenIddictConstants.Scopes.Roles))
            //{
            claims["roles"] = JArray.FromObject(await _userManager.GetRolesAsync(user));
            //}

            claims[OpenIdConnectConstants.Claims.Name] = await _userManager.GetUserNameAsync(user);

            claims["permissions"] = JArray.FromObject(User.Claims.Where(c => c.Type == UserClaimTypes.Permission).Select(c => c.Value));



            // Note: the complete list of standard claims supported by the OpenID Connect specification
            // can be found here: http://openid.net/specs/openid-connect-core-1_0.html#StandardClaims

            return Json(claims);
        }



    }
}