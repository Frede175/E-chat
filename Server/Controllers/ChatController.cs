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
using Server.Logging;
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

        private readonly IMessageService _messageService;

        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHubContext<ChatHub, IChatHub> _chatHub;

        private readonly IHubState<ChatHub, IChatHub> _chatHubState;

        private readonly ILogger<ChatController> _logger;

        private readonly IAuthorizationService _authorizationService;


        public ChatController(IChatService chatService, 
                              IMessageService messageService,
                              UserManager<ApplicationUser> userManager, 
                              ILogger<ChatController> logger, 
                              IHubContext<ChatHub, IChatHub> chathub,
                              IHubState<ChatHub, IChatHub> chatHubState,
                              IAuthorizationService authorizationService)
        {
            _userManager = userManager;
            _messageService = messageService;
            _chatService = chatService;
            _logger = logger;
            _chatHub = chathub;
            _chatHubState = chatHubState;
            _authorizationService = authorizationService;
        }

        [HttpGet("{chatId}", Name = "GetChat"), Produces("application/json")]
        [Authorize]
        public async Task<ActionResult> GetChat(int chatId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);
            var inChats = await _chatService.GetChatsAsync(_userManager.GetUserId(HttpContext.User));

            if (!inChats.Any(c => c.Id == chatId)) {
                var result = await _authorizationService.AuthorizeAsync(HttpContext.User, 
                            null, 
                            new PermissionsAuthorizationRequirement(
                                PermissionAttributeType.OR, 
                                Permission.CreateChat, 
                                Permission.AddUserToChat, 
                                Permission.RemoveUserFromChat
                            ));
                if (!result.Succeeded) {
                    _logger.LogWarning(LoggingEvents.Unauthorized, "{username} unauthorized to get chat {chatId} that the user is not in.", username, chatId);
                    return Unauthorized();
                }
            }

            _logger.LogInformation(LoggingEvents.GetItem, "{username} getting chat {chatId}.", username, chatId);
            var chat = await _chatService.GetSpecificChat(chatId);

            if (chat != null) {
                return Ok(new Chat(chat));
            }
            _logger.LogWarning(LoggingEvents.GetItemNotFound, "Chat with id {chatId} not found.", chatId);
            return NotFound();
        }

        // DELETE: https://localhost:5001/api/chat/{chatId}
        [HttpDelete("{chatId}"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveChat)]
        public async Task<ActionResult> RemoveChat(int chatId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} removing chat ({id})", username, chatId);

            var chat = await _chatService.GetSpecificChat(chatId);

            if (chat == null)
            {
                _logger.LogWarning(LoggingEvents.DeleteItemNotFound, "{username} removing chat ({id}), NOT FOUND", username, chatId);
                return NotFound();
            }

            if (await _chatService.RemoveChatAsync(chat)) 
            {
                await _chatHub.Clients.Group(chatId.ToString()).DeleteChat(chatId);
                return NoContent();
            }

            _logger.LogWarning(LoggingEvents.DeleteItemFail, "{username} failed removing chat ({id})", username, chatId);
            return BadRequest();
        }

        // GET: https://localhost:5001/api/chat/ 
        [HttpGet, Produces("application/json")]
        [RequiresPermissionAttribute(PermissionAttributeType.OR, Permission.RemoveChat)]
        public async Task<ActionResult<List<Chat>>> GetChats()
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting all chats.", username);
            return (await _chatService.GetChatsAsync()).Select(d => new Chat(d)).ToList();
        }
        
 

        // GET: https://localhost:5001/api/chat/available/{userId} 
        [HttpGet("available/{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.AddUserToChat)]
        public async Task<ActionResult<List<Chat>>> GetAvailableChats(string userId) 
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "Getting available chats for {username}", username);
            return (await _chatService.GetAvailableChatsAsync(userId)).Select(d => new Chat(d)).ToList();
        }



        // GET: https://localhost:5001/api/chat/user/{userId} 
        [HttpGet("user/{userId}"), Produces("application/json")]
        [Authorize]
        public async Task<ActionResult<List<Chat>>> GetUserChats(string userId)
        {
            
            var username = _userManager.GetUserName(HttpContext.User);

            var otherUser = await _userManager.FindByIdAsync(userId);
            if (_userManager.GetUserId(HttpContext.User) != userId) {
                 var result = await _authorizationService.AuthorizeAsync(HttpContext.User, 
                            null, 
                            new PermissionsAuthorizationRequirement(
                                PermissionAttributeType.OR, 
                                Permission.CreateChat, 
                                Permission.AddUserToChat, 
                                Permission.RemoveUserFromChat
                            ));
                
                if (!result.Succeeded) 
                {
                    _logger.LogInformation(LoggingEvents.Unauthorized, "User {username} is not authorized to get chats for user {otherUsername}.", username, otherUser.UserName);
                    return Unauthorized();
                }
                _logger.LogInformation(LoggingEvents.ListItems, "User {username} getting chats for {otherUserName}.", username, otherUser.UserName);
            } 
            else 
            {
                var basic = await _authorizationService.AuthorizeAsync(HttpContext.User, null, 
                                new PermissionsAuthorizationRequirement(PermissionAttributeType.AND, Permission.BasicPermissions));

                if (!basic.Succeeded) 
                {
                    _logger.LogInformation(LoggingEvents.Unauthorized, "User {username} does not have permission to get chats.", username);
                    return Unauthorized();
                }
                _logger.LogInformation(LoggingEvents.ListItems, "Getting chats for {username}.", username);
            }
            
            return (await _chatService.GetChatsAsync(userId)).Select(d => new Chat(d)).ToList();
        }

        // GET: https://localhost:5001/api/chat/private
        [HttpGet("private"), Produces("application/json")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult<List<Chat>>> GetPrivateChats()
        {
            var username = _userManager.GetUserName(HttpContext.User);
            var userId = _userManager.GetUserId(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting private chats.", username);
            return (await _chatService.GetPrivateChatsAsync(userId)).Select(d => new Chat(d)).ToList();
        }


        // POST: https://localhost:5001/api/chat/{departmentId}
        [HttpPost("{departmentId}")]
        [RequiresPermissionAttribute(permissions: Permission.CreateChat)]
        public async Task<ActionResult> CreateChat(int departmentId, [FromBody] Chat chat)
        {
            var userId = _userManager.GetUserId(HttpContext.User);
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} creating group chat.", username);
            var result = await _chatService.CreateChatAsync(new DbModels.Chat()
            {
                DepartmentId = departmentId,
                Name = chat.Name,
                IsGroupChat = true
            }, userId);
            if (result != null)
            {
                await _chatHubState.AddUserToGroupAsync(_chatHub, userId, result.Id.ToString());
                await _chatHub.Clients.Group(result.Id.ToString()).NewChat(new Chat(result));

                _logger.LogInformation(LoggingEvents.InsertItem, "{username} created group chat {chatName} ({chatId}).", username, result.Name, result.Id);
                
                await SendUpdateMessage(result.Id, userId, UpdateMessageType.ADD);


                return CreatedAtRoute(nameof(GetChat),new {chatId = result.Id} , new Chat(result));
            }
            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to create group chat {chatName}.", username, chat.Name);
            return BadRequest();
        }

        // POST: https://localhost:5001/api/chat/{departmentId}
        [HttpPost("private/{userId}")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult> CreatePrivateChat(string userId, [FromBody] Chat chat)
        {
            var currentUserId = _userManager.GetUserId(HttpContext.User);
            var currentUsername = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} creating private chat with user ({userid}).", currentUsername, userId);

            if (currentUserId == userId) 
            {
                _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} tried to create a chat with himself.", currentUsername);
                return BadRequest("Can't create a private with yourself.");
            }

            var otherUser = await _userManager.FindByIdAsync(userId);

            if (otherUser == null)
            {
                _logger.LogWarning(LoggingEvents.InsertItemNotFound, "{username} failed to create private, the other user ({userid}) does not exist.", currentUsername, userId);
                return NotFound("Other user does not exists.");
            }

            var existsResult = await _chatService.PrivateChatExists(userId, currentUserId);

            if (existsResult == true) 
            {
                _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to create private chat with {otherUsername}, since the chat allready exists.", currentUsername, otherUser.UserName);
                return BadRequest($"A private chat allready exists between you and {otherUser.UserName}.");
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
                await _chatHub.Clients.Group(result.Id.ToString()).NewChat(new Chat(result));
                _logger.LogInformation(LoggingEvents.InsertItem, "{username} created a private chat ({id}) with {otherUsername}.", currentUsername, result.Id, otherUser.UserName);
                return CreatedAtRoute(nameof(GetChat), new { chatId = result.Id }, new Chat(result));
            }

            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to create a private chat with {otherUsername}.", currentUsername, otherUser.UserName);
            return BadRequest();
        }


        // POST: https://localhost:5001/api/chat/leave/{chatId}
        [HttpPost("leave/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult> Leave(int chatId)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} leaving chat ({id}).", username, chatId);
            var user = await _userManager.GetUserAsync(User);

            if (user == null) 
            {
                _logger.LogWarning(LoggingEvents.DeleteItem, "{username} failed leaving chat ({id}).", username, chatId);
                return BadRequest();
            }

            var result = (await _chatService.RemoveUsersFromChatAsync(chatId, user.Id));

            if (result)
            {
                await _chatHub.Clients.Group(chatId.ToString()).Leave(chatId, new User(user));
                await SendUpdateMessage(chatId, user, UpdateMessageType.LEAVE);
                await _chatHubState.RemoveUserFromGroupAsync(_chatHub, user.Id, chatId.ToString());
                return NoContent();
            }

            _logger.LogWarning(LoggingEvents.DeleteItem, "{username} failed leaving chat ({id}).", username, chatId);
            return BadRequest();
        }


        // POST: https://localhost:5001/api/chat/add/{chatId}
        [HttpPost("add/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.AddUserToChat)]
        public async Task<ActionResult> AddUserToChat(int chatId, [FromBody] string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.InsertItem, "{username} adding user to chat ({id}).", username, chatId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null)
            {
                _logger.LogWarning(LoggingEvents.InsertItemNotFound, "{username} failed to add user to chat ({id}), since the user does not exist.", username, chatId);
                return NotFound("User");
            }

            var result = (await _chatService.AddUsersToChatAsync(chatId, userId));

            if (result)
            {
                await _chatHubState.AddUserToGroupAsync(_chatHub, userId, chatId.ToString());
                await _chatHub.Clients.Group(chatId.ToString()).Add(chatId, new User(user));
                await SendUpdateMessage(chatId, user, UpdateMessageType.ADD);

                return NoContent();
            }
            _logger.LogWarning(LoggingEvents.InsertItemFail, "{username} failed to add {other} to chat.", username, user.UserName);
            return BadRequest();
        }



        // POST: https://localhost:5001/api/chat/remove/{chatId}
        [HttpPost("remove/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.RemoveUserFromChat)]
        public async Task<ActionResult> RemoveUserFromChat(int chatId, [FromBody] string userId)
        {
            var username = _userManager.GetUserName(HttpContext.User);

            _logger.LogInformation(LoggingEvents.DeleteItem, "{username} removing user from chat ({id}).", username, chatId);

            var user = await _userManager.FindByIdAsync(userId);

            if (user == null) 
            {
                _logger.LogWarning(LoggingEvents.DeleteItemNotFound, "{username} removing user from chat ({id}), NOT FOUND", username, chatId);
                return NotFound("User");
            }

            
            var result = await _chatService.RemoveUsersFromChatAsync(chatId, userId);

            if (result)
            {
                await _chatHub.Clients.Group(chatId.ToString()).Remove(chatId, new User(user));
                await _chatHubState.RemoveUserFromGroupAsync(_chatHub, userId, chatId.ToString());
                await SendUpdateMessage(chatId, user, UpdateMessageType.REMOVE);

                return NoContent();
            }

            _logger.LogInformation(LoggingEvents.DeleteItemFail, "{username} failed to remove {other} from chat ({id}).", username, user.UserName, chatId);
            return BadRequest();
        }


        // GET: https://localhost:5001/api/chat/users/{chatId}
        [HttpGet ("users/{chatId}")]
        [RequiresPermissionAttribute(permissions: Permission.BasicPermissions)]
        public async Task<ActionResult> GetUsersInChat(int chatId)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting users in chat ({id}).", username, chatId);
            var chat = await _chatService.GetSpecificChat(chatId);
            if (chat != null)
            {
                var result = (await _chatService.GetUsersInChat(chatId)).Select(c => new User(c)).ToList();
                return Ok(result);
            }
            _logger.LogWarning(LoggingEvents.ListItemsNotFound, "{username} failed to get users in chat ({id}), since chat does not exist.", username, chatId);
            return NotFound("Chat");
        }

#region helpers

        private async Task SendUpdateMessage(int chatId, string userId, UpdateMessageType type) 
        {
            var message = await _messageService.SendUpdateMessageAsync(chatId, userId, type);
            if (message != null) {
                await _chatHub.Clients.Group(chatId.ToString()).ReceiveMessage(new Message(message));
            }
        }

        private async Task SendUpdateMessage(int chatId, ApplicationUser user, UpdateMessageType type) 
        {
            var message = await _messageService.SendUpdateMessageAsync(chatId, user, type);
            if (message != null) {
                await _chatHub.Clients.Group(chatId.ToString()).ReceiveMessage(new Message(message));
            }
        }

#endregion



    }
}
