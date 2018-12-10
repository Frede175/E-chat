using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using Server.Context;
using Server.Hubs;
using Server.Logging;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class DepartmentController : Controller
    {

        private readonly IDepartmentService _departmentService;

        private readonly IChatService _chatService;

        private readonly IHubState<ChatHub, IChatHub> _chatHubState;

        private readonly IHubContext<ChatHub, IChatHub> _chatHub;

        private readonly UserManager<ApplicationUser> _userManager;

        private readonly ILogger<DepartmentController> _logger;

        private readonly IAuthorizationService _authorizationService;


        public DepartmentController(IDepartmentService departmentService, 
            IHubContext<ChatHub, IChatHub> chatHub,
            IHubState<ChatHub, IChatHub> chatHubState,
            IChatService chatService,
            UserManager<ApplicationUser> userManager, 
            ILogger<DepartmentController> logger,
            IAuthorizationService authorizationService)
        {
            _departmentService = departmentService;
            _chatHub = chatHub;
            _chatHubState = chatHubState;
            _chatService = chatService;
            _userManager = userManager;
            _logger = logger;
            _authorizationService = authorizationService;

        }

        // GET: https://localhost:5001/api/Department/{departmentId}
        [HttpGet("{departmentId}"), Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateDepartment, Permission.UpdateDepartment, Permission.AddUserToDepartment, Permission.RemoveUserFromDepartment, Permission.DeleteDepartment)]
        public async Task<ActionResult<ICollection<Department>>> GetDepartment(int departmentId)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.GetItem, "{username} getting department ({id}).", username, departmentId);

            var department = await _departmentService.GetSpecificDepartmentAsync(departmentId);
            if (department == null) {
                _logger.LogWarning(LoggingEvents.GetItemNotFound, "{username} failed to get department ({id}).", username, departmentId);
                return NotFound("Department");
            }

            _logger.LogInformation(LoggingEvents.GetItem, "{username} got department ({name}).", username, department.Name);
            return Ok(new Department(department));

        }


        // GET: https://localhost:5001/api/Department
        [HttpGet, Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateDepartment, Permission.UpdateDepartment, Permission.AddUserToDepartment, Permission.RemoveUserFromDepartment, Permission.DeleteDepartment, Permission.CreateChat)]
        public async Task<ActionResult<ICollection<Department>>> GetDepartments()
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting all departments.", username);
            return (await _departmentService.GetDepartmentsAsync()).Select(d => new Department(d)).ToList();

        }

        // GET: https://localhost:5001/api/Department/{id} 
        [HttpGet("user/{userId}"), Produces("application/json")]
        [Authorize]
        public async Task<ActionResult<ICollection<Department>>> GetUserDepartments(string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            if (_userManager.GetUserId(HttpContext.User) != userId) {

                _logger.LogInformation(LoggingEvents.ListItems, "{username} getting departments for user ({id}).", username, userId);

                var user = await _userManager.FindByIdAsync(userId);

                if (user == null) 
                {
                    _logger.LogWarning(LoggingEvents.ListItemsNotFound, "{username} failed to get dpeartments for user ({id}), since the user does not exist.", username, userId);
                    return NotFound("user");
                }

                _logger.LogInformation(LoggingEvents.ListItems, "{username} getting departments for user {name}", username, user.UserName);
                 var result = await _authorizationService.AuthorizeAsync(HttpContext.User, 
                            null, 
                            new PermissionsAuthorizationRequirement(
                                PermissionAttributeType.OR, 
                                Permission.RemoveUserFromDepartment
                            ));
                if (!result.Succeeded) 
                {
                    _logger.LogWarning(LoggingEvents.Unauthorized, "{username} does not have permission to get departments for user {name}.", username, user.UserName);
                    return Unauthorized();
                }
            } 
            else 
            {
                _logger.LogInformation(LoggingEvents.ListItems, "{username} getting departments.", username);
                var basic = await _authorizationService.AuthorizeAsync(HttpContext.User, null, 
                                new PermissionsAuthorizationRequirement(PermissionAttributeType.AND, Permission.BasicPermissions));
                if (!basic.Succeeded) 
                {
                    _logger.LogWarning(LoggingEvents.Unauthorized, "{username} does not have permission to get departments.", username);
                    return Unauthorized();
                }
            }

            return (await _departmentService.GetDepartmentsAsync(userId)).Select(d => new Department(d)).ToList();
        }

        [HttpPost]
        [RequiresPermissionAttribute(permissions: Permission.CreateDepartment)]
        public async Task<IActionResult> CreateDepartment([FromBody] Department department)
        {
            var d = new DbModels.Department()
            {
                Name = department.Name
            };

            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} creating department with name {name}", username, d.Name);

            var result = await _departmentService.CreateDepartmentAsync(d);
            if (result != null)
            {
                _logger.LogInformation(LoggingEvents.InsertItem, "{username} created department with name {name}", username, result.Name);
                return CreatedAtAction(nameof(GetDepartments), new { departmentId = result.Id }, new Department(result));
            }

            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to create department with name {name}", username, d.Name);
            return BadRequest();
        }

        // POST: https://localhost:5001/api/Department/{departmentId}
        [HttpPost("{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.AddUserToDepartment)]
        public async Task<IActionResult> AddUserToDepartment(int departmentId, [FromBody] string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} adding user to department ({id}).", username, departmentId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null) 
            {
                _logger.LogWarning(LoggingEvents.InsertItemNotFound, "{usernamed} failed to add user to department ({id}), since the user does not exist.", username, departmentId);
                return NotFound("User");
            }


            var result = await _departmentService.AddUsersToDepartmentAsync(departmentId, user);
            if (result)
            {
                _logger.LogInformation(LoggingEvents.InsertItem, "{username} adding {other} to department ({id}).", username, user.UserName, departmentId);
                return NoContent();
            }
            _logger.LogWarning(LoggingEvents.InsertItem, "{username} failed adding {other} to department ({id}).", username, user.UserName, departmentId);
            return BadRequest();

        }

        // POST: https://localhost:5001/api/Department/remove/{departmentId}
        [HttpPost("remove/{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveUserFromDepartment)]
        public async Task<IActionResult> RemoveUserFromDepartment(int departmentId, [FromBody] string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} removing user from department ({id}).", username, departmentId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null)
            {
                _logger.LogWarning(LoggingEvents.InsertItemNotFound, "{usernamed} failed to remove user from department ({id}), since the user does not exist.", username, departmentId);
                return NotFound("User");
            }

            var result = await _departmentService.RemoveUsersFromDepartmentAsync(departmentId, user);
            if (result)
            {
                var chatIds = (await _chatService.GetChatsAsync(userId, departmentId)).Select(c => c.Id);

                foreach (var chatId in chatIds) {
                    await _chatService.RemoveUsersFromChatAsync(chatId, userId);
                    await _chatHub.Clients.Group(chatId.ToString()).Remove(chatId, new User(user));
                    await _chatHubState.RemoveUserFromGroupAsync(_chatHub, userId, chatId.ToString());
                }

                _logger.LogInformation(LoggingEvents.InsertItem, "{username} removing {other} from department ({id}).", username, user.UserName, departmentId);
                return NoContent();
            }
            _logger.LogWarning(LoggingEvents.InsertItem, "{username} failed removing {other} from department ({id}).", username, user.UserName, departmentId);
            return BadRequest();
        }


        // DELETE: https://localhost:5001/api/Department/{departmentId}
        [HttpDelete("{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.DeleteDepartment)]
        public async Task<ActionResult> RemoveDepartment(int departmentId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} removing department ({id}).", username, departmentId);

            var result = await _departmentService.RemoveDepartmentAsync(departmentId);
            if (result)
            {
                return NoContent();
            }
            _logger.LogWarning(LoggingEvents.DeleteItemFail, "{username} failed to delete department ({id}).", username, departmentId);
            return BadRequest();
        }




        //PUT: https://localhost:5001/api/Department/{depId}
        [HttpPut("{depId}")]
        [RequiresPermissionAttribute(permissions: Permission.UpdateDepartment)]
        public async Task<ActionResult> UpdateDepartment(int depId, [FromBody] string newName)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.UpdateItem, "{username} updating department ({id}).", username, depId);

            var dep = await _departmentService.GetSpecificDepartmentAsync(depId);

            if (dep == null)
            {
                _logger.LogWarning(LoggingEvents.UpdateItemNotFound, "{username} updating department ({id}) failed, since the department does not exist.", username, depId);
                return NotFound("Department");
            }

            dep.Name = newName;

            var result = await _departmentService.UpdateDepartmentAsync(dep);

            if (result)
            {
                return NoContent();
            }
            _logger.LogWarning(LoggingEvents.UpdateItemFail, "{username} failed to update department ({id}).", username, depId);
            return BadRequest();
        }

        // GET: https://localhost:5001/api/department/users/{departmentId}
        [HttpGet("users/{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveUserFromDepartment)]
        public async Task<ActionResult<List<User>>> GetUsersInDepartment(int departmentId)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting users in department ({id}).", username, departmentId);
            return Ok((await _departmentService.GetUsersInDepartmentsAsync(departmentId)).Select(u => new User(u)).ToList());
        }

        // GET: https://localhost:5001/api/department/available/{userId} 
        [HttpGet("available/{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.AddUserToDepartment)]
        public async Task<ActionResult<List<Department>>> GetAvailableDepartments(string userId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting available departments for user ({userid}).", username, userId);
            return (await _departmentService.GetAvailableDepartmentsAsync(userId)).Select(d => new Department(d)).ToList();
        }

    }
}