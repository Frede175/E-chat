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

namespace Server.Hubs
{
    [Authorize(AuthenticationSchemes = OpenIddictValidationDefaults.AuthenticationScheme)]
    public class ChatHub : Hub<IChatHub>
    {

        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;

        private readonly IHubState<ChatHub, IChatHub> _hubState;

        private readonly ILogger<ChatHub> _logger;

        public ChatHub(IChatService chat, UserManager<ApplicationUser> userManager, IHubState<ChatHub, IChatHub> hubState, ILogger<ChatHub> logger)
        {
            _chatService = chat;
            _userManager = userManager;
            _hubState = hubState;
            _logger = logger;
        }


        public async Task SendMessage(MessageIn message)
        {
            var userId = _userManager.GetUserId(Context.User);

            var returnMessage = await _chatService.SendMessageAsync(message.ChatId, userId, message.Content);
            if(returnMessage != null){
                await Clients.Group(message.ChatId.ToString()).ReceiveMessage(new Message(returnMessage));
            }

        }

        public async override Task OnConnectedAsync()
        {
            var userId = _userManager.GetUserId(Context.User);

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
        }

        
        public override Task OnDisconnectedAsync(Exception exception) {
            var userId = _userManager.GetUserId(Context.User);

            _hubState.Connections[userId].Remove(Context.ConnectionId);

            if (!_hubState.Connections[userId].Any()) {
                _hubState.Connections.Remove(userId);
            }

            return Task.CompletedTask;
        }

    }
}