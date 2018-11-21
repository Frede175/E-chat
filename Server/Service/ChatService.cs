using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.DbModels;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class ChatService : IChatService
    {
        private readonly DbSet<Message> _messages;
        private readonly DbSet<Chat> _chats;
        private readonly ApplicationDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;

        private readonly DbSet<UserChat> _userChat;


        public ChatService(ApplicationDbContext context, UserManager<ApplicationUser> userManager)
        {
            _messages = context.Set<Message>();
            _chats = context.Set<Chat>();
            _context = context;
            _userChat = context.Set<UserChat>();
            _userManager = userManager;

        }


        public async Task<Chat> GetChatAsync(int chatId) {
            return await _chats.FindAsync(chatId);
        }


        /// <summary>
        /// Method for adding a new user to a chat
        /// </summary>
        /// <returns>The users to chat async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="users">Users.</param>
        public async Task<bool> AddUsersToChatAsync(int chatId, params string[] userIds)
        {
            var chat = await _chats.FindAsync(chatId);
            var users = _userManager.Users.Include(u => u.UserDepartments).Where(u => userIds.Contains(u.Id));
            int count = 0;
            if (chat != null)
            {
                foreach (var user in users)
                {
                    if (user.UserDepartments.Any(u => u.DepartmentId == chat.DepartmentId)) {
                        _userChat.Add(new UserChat() { UserId = user.Id, ChatId = chat.Id });
                        count++;
                    }
                    
                }
                if (count != 0) {
                    var result = await _context.SaveChangesAsync();
                    if (result == count)
                    {
                        return true;
                    }
                }
                
            }

            return false;
        }


        /// <summary>
        /// Method for finding out wether a chat is active or not (has active users)
        /// </summary>
        /// <returns>The is active async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        public async Task<bool> ChatIsActiveAsync(int chatId)
        {
            var chat = await _chats.FindAsync(chatId);
            if (chat != null)
            {
                return _userChat.Any(c => c.ChatId == chat.Id);
            }
            return false;
        }


        /// <summary>
        /// Method for Creating a new chat
        /// </summary>
        /// <returns>The chat async.</returns>
        /// <param name="chat">Chat.</param>
        public async Task<Chat> CreateChatAsync(Chat chat, string userId)
        {
            chat.UserChats = new List<UserChat>() { new UserChat { UserId = userId } };
            _chats.Add(chat);

            var result = await _context.SaveChangesAsync();

            if (result == 2)
            {
                return chat;
            }
            return null;
        }


        /// <summary>
        /// Method for inviting a user who is not in the chat
        /// </summary>
        /// <returns>The to chat async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="user">User.</param>
        public async Task<bool> InviteToChatAsync(int chatId, ApplicationUser user)
        {
            var chat = await _chats.FindAsync(chatId);
            if (!chat.UserChats.Any(u => u.UserId == user.Id))
            {
                _userChat.Add(new UserChat() { UserId = user.Id, ChatId = chat.Id });
                _chats.Update(chat);
                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    return true;
                }
            }

            return false;
        }


        /// <summary>
        /// Method for deleting/removing a chat for a specific user
        /// </summary>
        /// <returns>The chat async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="user">User.</param>
        public async Task<bool> RemoveChatAsync(int chatId, ApplicationUser user)
        {
            var chat = await _chats.FindAsync(chatId);
            if (chat != null)
            {
                _userChat.Remove(new UserChat() { UserId = user.Id, ChatId = chat.Id });

                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    return true;
                }
            }
            return false;
        }


        /// <summary>
        /// Method for removing a user from a specific chat
        /// </summary>
        /// <returns>The users from chat async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="users">Users.</param>
        public async Task<bool> RemoveUsersFromChatAsync(int chatId, params string[] userIds)
        {
            var chat = await _chats.Include(r => r.UserChats).FirstOrDefaultAsync(c => c.Id == chatId);

            if (chat != null)
            {
                foreach (string userId in userIds)
                {
                    if (chat.UserChats.Any(u => u.UserId == userId))
                    {
                        _userChat.Remove(new UserChat() { UserId = userId, ChatId = chat.Id });
                    }
                }
                _chats.Update(chat);
                var result = await _context.SaveChangesAsync();
                if (result == userIds.Count())
                {
                    return true;
                }
            }
            return false;

        }


        /// <summary>
        /// Method for retrieving list of chats that a specific user is part of
        /// </summary>
        /// <returns>The chats async.</returns>
        /// <param name="user">User.</param>
        public async Task<ICollection<Chat>> GetChatsAsync(string userId, int departmentId)
        {
            return await _chats.Cast<Chat>()
                               .Where(c => c.Department.Id == departmentId &&
                                      c.UserChats.Any(u => u.UserId == userId)).ToListAsync();
        }

        public async Task<ICollection<Chat>> GetChatsAsync(string userId)
        {
            return await _chats.Cast<Chat>().Where(c => c.UserChats.Any(u => u.UserId == userId)).ToListAsync();
        }

        public async Task<ICollection<Message>> RetrieveMessagesAsync(int chatId, int page, int pageSize)
        {
            var chat = await _chats.FindAsync(chatId);

            if (chat != null)
            {

                return await _messages.Include(m => m.ApplicationUser).Where(c => c.ChatId == chat.Id).OrderByDescending(m => m.TimeStamp).Skip(pageSize * page).Take(pageSize).ToListAsync();
            }

            return null;
        }


        /// <summary>
        /// Method for sending a message in a chat
        /// </summary>
        /// <returns>The message async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="message">Message.</param>
        public async Task<Message> SendMessageAsync(int chatId, string userId, string content)
        {
            var chat = await _chats.FindAsync(chatId);
            if (!string.IsNullOrEmpty(content) && chat != null)
            {
                var message = new Message()
                {
                    Content = content,
                    TimeStamp = DateTime.UtcNow,
                    SenderId = userId,
                    ChatId = chat.Id
                };

                _messages.Add(message);
                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    await _context.Entry(message).Reference(m => m.ApplicationUser).LoadAsync();
                    return message;
                }
            }
            return null;
        }

        public async Task<Chat> GetSpecificChat(int chatId)
        {
            return await _chats.FindAsync(chatId);
        }

        public async Task<Chat> GetSpecificChat(int depId, string chatName)
        {
            return await _chats.Cast<Chat>().SingleOrDefaultAsync(c => string.Equals
                                                                  (c.Name, chatName, StringComparison.InvariantCultureIgnoreCase) &&
                                                                    c.DepartmentId == depId);
        }


        public async Task<List<ApplicationUser>> GetUsersInChat(int chatId)
        {
            return await _userChat.Where(u => u.ChatId == chatId).Select(u => u.ApplicationUser).ToListAsync();
        }

        public async Task<bool> PrivateChatExists(string userId, string userId2)
        {
            return await _chats.AnyAsync(c => !c.IsGroupChat && c.UserChats.Count(u => u.UserId == userId || u.UserId == userId2) == 2);
        }

        public async Task<Chat> CreatePrivateChat(Chat chat, string userId, string userId2)
        {
            chat.UserChats = new List<UserChat>() { new UserChat { UserId = userId }, new UserChat { UserId = userId2 } };
            _chats.Add(chat);

            var result = await _context.SaveChangesAsync();

            if (result == 3)
            {
                return chat;
            }
            return null;
        }

        public async Task<List<Chat>> GetPrivateChatsAsync(string userId)
        {
            return await _chats.Where(c => !c.IsGroupChat && c.UserChats.Any(u => u.UserId == userId)).ToListAsync();
        }
    }
}
