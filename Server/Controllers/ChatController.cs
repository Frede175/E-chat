using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Server.Context;
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

        private readonly ILogger<ChatController> _logger;


        public ChatController(IChatService chatService, UserManager<ApplicationUser> userManager, ILogger<ChatController> logger)
        {
            _userManager = userManager;
            _chatService = chatService;
            _logger = logger;

        }


        // GET: https://localhost:5001/api/chat/{userId} 
        [HttpGet("{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetChats)]
        public async Task<ActionResult<List<Chat>>> GetChats(string userId, int departmentId)
        {
            _logger.LogDebug("Department ID: " + departmentId);
            return (await _chatService.GetChatsAsync(userId, departmentId)).Select(d => new Chat(d)).ToList();
        }


        // POST: https://localhost:5001/api/chat/{departmentId}
        [HttpPost("{departmentId}")]
        [RequiresPermissionAttribute(Permission.CreateChat)]
        public async Task<ActionResult> CreateChat(int departmentId, [FromBody] Chat chat)
        {
            var result = await _chatService.CreateChatAsync(new DbModels.Chat()
            {
                DepartmentId = departmentId,
                Name = chat.Name
            }, _userManager.GetUserId(HttpContext.User));
            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // POST: https://localhost:5001/api/chat/leave/{chatId}
        [HttpPost("leave/{chatId}")]
        [RequiresPermissionAttribute(Permission.LeaveChat)]
        public async Task<ActionResult> Leave(int chatId)
        {
            var user = await _userManager.GetUserAsync(User);
            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, user.Id));

            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // POST: https://localhost:5001/api/chat/add/{chatId}
        [HttpPost("add/{chatId}")]
        [RequiresPermissionAttribute(Permission.AddUserToChat)]
        public async Task<ActionResult> AddUserToChat(int chatId, [FromBody] string userId)
        {
            _logger.LogDebug("User ID: " + userId);
            var result = (await _chatService.AddUsersToChatAsync(chatId, userId));

            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }



        // POST: https://localhost:5001/api/chat/remove/{chatId}
        [HttpPost("remove/{chatId}")]
        [RequiresPermissionAttribute(Permission.RemoveUserFromChat)]
        public async Task<ActionResult> RemoveUserFromChat(int chatId, string userId)
        {
            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, userId));

            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // GET: https://localhost:5001/api/chat/getUsers/
        [HttpGet]
        [RequiresPermissionAttribute(Permission.GetUsersInChat)]
        public async Task<List<User>> GetUsersInChat(int chatId)
        {
            var chat = await _chatService.GetSpecificChat(chatId);
            if (chat != null)
            {
                var result = (await _chatService.GetUsersInChat(chatId)).Select(c => new User()).ToList();
                if (result != null)
                {
                    return result;
                }
            }
            return null;
        }


    }
}
