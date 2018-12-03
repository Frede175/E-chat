using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
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

    public class MessagesController : Controller
    {
        private readonly IChatService _chatService;

        private readonly UserManager<ApplicationUser> _userManager;

        private readonly ILogger<MessagesController> _logger;

        public MessagesController (IChatService chatService, UserManager<ApplicationUser> userManager, ILogger<MessagesController> logger){
            _chatService = chatService;
            _userManager = userManager;
            _logger = logger;
        }

        // GET: https://localhost:5001/api/Messages/{chatId}
        [HttpGet("{chatId}"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult<ICollection<Message>>> GetMessages(int chatId, int pageNumber, int pageSize){
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting messages page {page}, count {coumt} from chat {id}.", username, pageNumber, pageSize, chatId);
            return (await _chatService.RetrieveMessagesAsync(chatId, pageNumber, pageSize)).Select(d => new Message(d)).ToList();
        }

        

    }
}