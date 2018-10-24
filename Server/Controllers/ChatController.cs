using System;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Server.Context;
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


    }
}
