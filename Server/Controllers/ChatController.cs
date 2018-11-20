using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using Server.Context;
using Server.Hubs;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class ChatController : Controller
    {
        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHubContext<ChatHub> _chatHub;

        private readonly IHubState<ChatHub> _chatHubState;

        private readonly ILogger<ChatController> _logger;


        public ChatController(IChatService chatService, 
                              UserManager<ApplicationUser> userManager, 
                              ILogger<ChatController> logger, 
                              IHubContext<ChatHub> chathub,
                              IHubState<ChatHub> chatHubState)
        {
            _userManager = userManager;
            _chatService = chatService;
            _logger = logger;
            _chatHub = chathub;
            _chatHubState = chatHubState;
        }

        [HttpGet("{chatId}", Name = "GetChat"), Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateChat, Permission.AddUserToChat, Permission.RemoveUserFromChat)]
        public async Task<ActionResult> GetChat(int chatId) {
            var chat = await _chatService.GetSpecificChat(chatId);

            if (chat != null) {
                return Ok(new Chat(chat));
            }
            return NotFound();
        }


        // GET: https://localhost:5001/api/chat/user/{userId} 
        [HttpGet("user/{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.CreateChat, Permission.AddUserToChat, Permission.RemoveUserFromChat)]
        public async Task<ActionResult<List<Chat>>> GetChats(string userId, int departmentId)
        {
            return (await _chatService.GetChatsAsync(userId, departmentId)).Select(d => new Chat(d)).ToList();
        }

        // GET: https://localhost:5001/api/chat/private/{userId} 
        [HttpGet("private/{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult<List<Chat>>> GetPrivateChats(string userId)
        {
            return (await _chatService.GetPrivateChatsAsync(userId)).Select(d => new Chat(d)).ToList();
        }


        // POST: https://localhost:5001/api/chat/{departmentId}
        [HttpPost("{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.CreateChat)]
        public async Task<ActionResult> CreateChat(int departmentId, [FromBody] Chat chat)
        {
            var userId = _userManager.GetUserId(HttpContext.User);
            var result = await _chatService.CreateChatAsync(new DbModels.Chat()
            {
                DepartmentId = departmentId,
                Name = chat.Name,
                IsGroupChat = true
            }, userId);
            if (result != null)
            {
                await _chatHubState.AddUserToGroupAsync(_chatHub, userId, result.Id.ToString());
                await _chatHub.Clients.Group(result.Id.ToString()).SendAsync("NewChat", new Chat(result));
                return CreatedAtRoute(nameof(GetChat),new {chatId = result.Id} , new Chat(result));
            }

            return new BadRequestResult();
        }

        // POST: https://localhost:5001/api/chat/{departmentId}
        [HttpPost("private/{userId}")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult> CreatePrivateChat(string userId, [FromBody] Chat chat)
        {
            var currentUserId = _userManager.GetUserId(HttpContext.User);
            var existsResult = await _chatService.PrivateChatExists(userId, currentUserId);

            if (existsResult == true || currentUserId == userId) 
            {
                return BadRequest();
            }

            var result = await _chatService.CreatePrivateChat(new DbModels.Chat()
            {
                Name = chat.Name,
                IsGroupChat = false,
                
            }, currentUserId, userId);

            if (result != null)
            {
                //Add hub connections to group
                await _chatHubState.AddUserToGroupAsync(_chatHub, userId, result.Id.ToString());
                await _chatHubState.AddUserToGroupAsync(_chatHub, currentUserId, result.Id.ToString());
                await _chatHub.Clients.Group(result.Id.ToString()).SendAsync("NewChat", new Chat(result));
                return CreatedAtRoute(nameof(GetChat), new { chatId = result.Id }, new Chat(result));
            }

            return BadRequest();
        }


        // POST: https://localhost:5001/api/chat/leave/{chatId}
        [HttpPost("leave/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.LeaveChat)]
        public async Task<ActionResult> Leave(int chatId)
        {
            var user = await _userManager.GetUserAsync(User);
            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, user.Id));

            if (result)
            {
                await _chatHub.Clients.Group(chatId.ToString()).SendAsync("Leave", chatId, new User(user));
                await _chatHubState.RemoveUserFromGroupAsync(_chatHub, user.Id, chatId.ToString());
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // POST: https://localhost:5001/api/chat/add/{chatId}
        [HttpPost("add/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.AddUserToChat)]
        public async Task<ActionResult> AddUserToChat(int chatId, [FromBody] string userId)
        {
            var result = (await _chatService.AddUsersToChatAsync(chatId, userId));
            var user = await _userManager.FindByIdAsync(userId);

            if (result)
            {
                await _chatHubState.AddUserToGroupAsync(_chatHub, userId, chatId.ToString());
                await _chatHub.Clients.Group(chatId.ToString()).SendAsync("Add", chatId, new User(user));
                return new OkResult();
            }

            return new BadRequestResult();
        }



        // POST: https://localhost:5001/api/chat/remove/{chatId}
        [HttpPost("remove/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveUserFromChat)]
        public async Task<ActionResult> RemoveUserFromChat(int chatId, [FromBody] string userId)
        {
            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, userId));
            var user = await _userManager.FindByIdAsync(userId);

            if (result)
            {
                await _chatHub.Clients.Group(chatId.ToString()).SendAsync("Remove", chatId, new User(user));
                await _chatHubState.RemoveUserFromGroupAsync(_chatHub, userId, chatId.ToString());
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // GET: https://localhost:5001/api/chat/users/{chatId}
        [HttpGet ("users/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<List<User>> GetUsersInChat(int chatId)
        {
            var chat = await _chatService.GetSpecificChat(chatId);
            if (chat != null)
            {
                var result = (await _chatService.GetUsersInChat(chatId)).Select(c => new User(c)).ToList();
                return result;
            }
            return null;
        }


    }
}
