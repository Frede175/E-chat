using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
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
    public class ChatController
    {
        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;


        public ChatController(IChatService chatService, , UserManager<ApplicationUser> userManager)
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




    }
}
