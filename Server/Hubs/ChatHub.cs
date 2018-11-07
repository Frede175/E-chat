using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using OpenIddict.Validation;
using Microsoft.AspNetCore.Http;
using Server.Context;
using Server.Service.Interfaces;
using System.Linq;
using Server.Models;

namespace Server.Hubs
{
    [Authorize(AuthenticationSchemes = OpenIddictValidationDefaults.AuthenticationScheme)]
    public class ChatHub : Hub
    {

        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;

        public ChatHub(IChatService chat, UserManager<ApplicationUser> userManager)
        {
            _chatService = chat;
            _userManager = userManager;

        }


        public async Task SendMessage(MessageIn message)
        {
            var userId = _userManager.GetUserId(Context.User);

            var returnMessage = await _chatService.SendMessageAsync(message.ChatId, userId, message.Content);
            if(returnMessage != null){
                await Clients.Group(message.ChatId.ToString()).SendAsync("ReceiveMessage", new Message(returnMessage, message.ChatId));
            }

        }


        public async override Task OnConnectedAsync()
        {
            var userId = _userManager.GetUserId(Context.User);
            var chats = await _chatService.GetChatsAsync(userId);
            var chatIds = chats.Select(c => c.Id);

            foreach (int chatId in chatIds)
            {
                await Groups.AddToGroupAsync(Context.ConnectionId, chatId.ToString());
            }
        }


    }
}