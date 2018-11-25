using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.DbModels;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class MessageService : IMessageService
    {


        private readonly DbSet<Chat> _chats;

        private readonly DbSet<Message> _messages;

        private readonly UserManager<ApplicationUser> _userManager;

        private readonly ApplicationDbContext _context;


        public MessageService(ApplicationDbContext context, UserManager<ApplicationUser> userManager) 
        {
            _context = context;
            _userManager = userManager;
            _chats = _context.Set<Chat>();
            _messages = _context.Set<Message>();
        }        




        public async Task<Message> SendMessageAsync(int chatId, string userId, string content)
        {
            if (string.IsNullOrEmpty(content)) return null;

            var chat = await _chats.FindAsync(chatId);

            if (chat == null) return null;

            var user = await _userManager.Users.Include(u => u.UserChats).SingleOrDefaultAsync(u => u.Id == userId);

            if (user == null) return null;

            if (user.UserChats.Any(uc => uc.ChatId == chat.Id))
            {
                var message = new Message()
                {
                    Content = content,
                    TimeStamp = DateTime.UtcNow,
                    SenderId = userId,
                    ApplicationUser = user,
                    ChatId = chat.Id
                };

                _messages.Add(message);
                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    return message;
                }
            }
            return null;
        }

        public async Task<Message> SendUpdateMessageAsync(int chatId, ApplicationUser user, UpdateMessageType type)
        {
            var chat = await _chats.FindAsync(chatId);

            if (chat != null)
            {
                string content = null;
                
                switch (type)
                {
                    case UpdateMessageType.ADD:
                        content = $"{user.UserName} joined the chat.";
                        break;
                    case UpdateMessageType.LEAVE:
                        content = $"{user.UserName} left the chat.";
                        break;
                    case UpdateMessageType.REMOVE:
                        content = $"{user.UserName} has been removed from the chat.";
                        break;
                }



                var message = new Message() 
                {
                    Content = content,
                    TimeStamp = DateTime.UtcNow,
                    SenderId = user.Id,
                    ApplicationUser = user,
                    ChatId = chatId
                };

                _messages.Add(message);
                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    return message;
                }
            }
            
            return null;
        }

        public async Task<Message> SendUpdateMessageAsync(int chatId, string userId, UpdateMessageType type) 
        {
            var user = await _userManager.FindByIdAsync(userId);

            return await SendUpdateMessageAsync(chatId, user, type);
        }
        
    }
}