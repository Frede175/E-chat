using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Server.Context;
using Server.Models;
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


        public ChatController(IChatService chatService, UserManager<ApplicationUser> userManager)
        {
            _userManager = userManager;
            _chatService = chatService;

        }


        // GET: https://localhost:5001/api/chat/ 
        [HttpGet("{userId}")]
        public async Task<ActionResult<List<Chat>>> GetChats(string userId, int departmenId)
        {
            return (await _chatService.RetrieveChatsAsync(userId, departmenId)).Select(d => new Chat(d)).ToList();
        }


        // POST: https://localhost:5001/api/chat/
        [HttpPost("{departmentId}")]
        public async Task<ActionResult> CreateChat(int departmentId, [FromBody] Chat chat){
            var result = await _chatService.CreateChatAsync(new DbModels.Chat()
            {
                DepartmentId = departmentId,
                Name = chat.Name
            });
            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }


        // POST: https://localhost:5001/api/chat/leave/{chatId}
        [Route("[action]")]
        [HttpPost("{chatId}", Name = "leave")]
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
        [Route("[action]")]
        [HttpPost("{chatId}", Name = "add")]
        public async Task<ActionResult> AddUserToChat(int chatId, string userId)
        {
            var result = (await _chatService.AddUsersToChatAsync(chatId, userId));

            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();
        }



        // POST: https://localhost:5001/api/chat/remove/{chatId}
        [Route("{action}")]
        [HttpPost("{chatId}", Name = "remove")]
        public async Task<ActionResult> RemoveUserFromChat(int chatId, string userId)
        {
            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, userId));

            if (result)
            {
                return new OkResult();
            }

            return new BadRequestResult();

        }
    }
}
