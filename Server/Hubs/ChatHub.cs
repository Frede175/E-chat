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
using System.Collections.Generic;
using System;
using Microsoft.Extensions.Logging;
using Server.Security;
using Server.Logging;

namespace Server.Hubs
{
    [Authorize(AuthenticationSchemes = OpenIddictValidationDefaults.AuthenticationScheme)]
    public class ChatHub : Hub<IChatHub>
    {

        private readonly IMessageService _messageService;

        private readonly IChatService _chatService;

        private readonly UserManager<ApplicationUser> _userManager;

        private readonly IHubState<ChatHub, IChatHub> _hubState;

        private readonly ILogger<ChatHub> _logger;

        public ChatHub(IMessageService messageService,
            IChatService chatService, 
            UserManager<ApplicationUser> userManager, 
            IHubState<ChatHub, IChatHub> hubState,
            ILogger<ChatHub> logger)
        {
            _messageService = messageService;
            _chatService = chatService;
            _userManager = userManager;
            _hubState = hubState;
            _logger = logger;
        }


        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task SendMessage(MessageIn message)
        {
            var userId = _userManager.GetUserId(Context.User);

            var returnMessage = await _messageService.SendMessageAsync(message.ChatId, userId, message.Content);
            if(returnMessage != null){
                var userName = _userManager.GetUserName(Context.User);
                _logger.LogInformation(LoggingEvents.SendMessage, $"{userName} send a message: {message.Content}. To chat {returnMessage.Chat.Name}.");
                await Clients.Group(message.ChatId.ToString()).ReceiveMessage(new Message(returnMessage));
            }

        }

        public async override Task OnConnectedAsync()
        {
            var userId = _userManager.GetUserId(Context.User);
            var userName = _userManager.GetUserName(Context.User);

            _logger.LogInformation(LoggingEvents.ConnectedToHub, $"{userName} connected to ChatHub.");

            if (!_hubState.Connections.ContainsKey(userId)) {
                _hubState.Connections.Add(userId, new List<string>());
            }
            _hubState.Connections[userId].Add(Context.ConnectionId);

            var chats = await _chatService.GetChatsAsync(userId);
            var chatIds = chats.Select(c => c.Id);

            foreach (int chatId in chatIds)
            {
                await Groups.AddToGroupAsync(Context.ConnectionId, chatId.ToString());
            }

            await base.OnConnectedAsync();
        }

        
        public async override Task OnDisconnectedAsync(Exception exception) {
            var userId = _userManager.GetUserId(Context.User);
            var userName = _userManager.GetUserName(Context.User);

            _logger.LogInformation(LoggingEvents.DisconnectedFromHub, $"{userName} disconnected from ChatHub.");

            _hubState.Connections[userId].Remove(Context.ConnectionId);

            if (!_hubState.Connections[userId].Any()) {
                _hubState.Connections.Remove(userId);
            }

            await base.OnDisconnectedAsync(exception);
        }
        

    }
}