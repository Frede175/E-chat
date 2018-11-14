using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Server.Models;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]

    public class MessagesController
    {
        private readonly IChatService _chatService;


        public MessagesController (IChatService chatService){
            _chatService = chatService;
        }

        // GET: https://localhost:5001/api/Messages/{chatId}
        [HttpGet("{chatId}"), Produces("application/json")]
        public async Task<ActionResult<ICollection<Message>>> GetMessages(int chatId, int pageNumber, int pageSize){
            return (await _chatService.RetrieveMessagesAsync(chatId, pageNumber, pageSize)).Select(d => new Message(d)).ToList();
        }

        

    }
}