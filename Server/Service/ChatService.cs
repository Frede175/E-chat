using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.Models;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class ChatService : IChatService
    {
        private readonly DbSet<Message> _messages;
        private readonly DbSet<Chat> _chats;
        private readonly ApplicationDbContext _context;


        public ChatService(ApplicationDbContext context)
        {
            _messages = context.Set<Message>();
            _chats = context.Set<Chat>();
            _context = context;
        }


        /// <summary>
        /// Method for adding a new user to a chat
        /// </summary>
        /// <returns>The users to chat async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="users">Users.</param>
        public async Task<bool> AddUsersToChatAsync(int chatId, params ApplicationUser[] users)
        {
            var chat = await _chats.FindAsync(chatId);
            if (chat != null)
            {
                foreach (var user in users)
                {
                    chat.ApplicationUsers.Add(user);
                }
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
        /// Method for finding out wether a chat is active or not (has active users)
        /// </summary>
        /// <returns>The is active async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        public async Task<bool> ChatIsActiveAsync(int chatId)
        {
            var chat = await _chats.FindAsync(chatId);
            if (chat != null)
            {
                return chat.ApplicationUsers.Any();
            }
            return false;
        }


        /// <summary>
        /// Method for Creating a new chat
        /// </summary>
        /// <returns>The chat async.</returns>
        /// <param name="chat">Chat.</param>
        public async Task<bool> CreateChatAsync(Chat chat)
        {
            _chats.Add(chat);
            var result = await _context.SaveChangesAsync();

            if (result == 1)
            {
                return true;
            }
            return false;
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
            if (!chat.ApplicationUsers.Contains(user))
            {
                chat.ApplicationUsers.Add(user);
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
                chat.ApplicationUsers.Remove(user);

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
        public async Task<bool> RemoveUsersFromChatAsync(int chatId, params ApplicationUser[] users)
        {
            var chat = await _chats.FindAsync(chatId);

            if (chat != null)
            {
                foreach (var user in users)
                {
                    if (chat.ApplicationUsers.Contains(user))
                    {
                        chat.ApplicationUsers.Remove(user);
                    }
                }
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
        /// Method for retrieving list of chats that a specific user is part of
        /// </summary>
        /// <returns>The chats async.</returns>
        /// <param name="user">User.</param>
        public async Task<ICollection<Chat>> RetrieveChatsAsync(ApplicationUser user)
        {
            return await _chats.Cast<Chat>().Where(c => c.ApplicationUsers.Contains(user)).ToListAsync();
        }

        public async Task<ICollection<Message>> RetrieveMessagesAsync(int chatId)
        {
            var chat = await _chats.FindAsync(chatId);
            return chat.Messages.ToList();
        }


        /// <summary>
        /// Method for sending a message in a chat
        /// </summary>
        /// <returns>The message async.</returns>
        /// <param name="chatId">Chat identifier.</param>
        /// <param name="message">Message.</param>
        public async Task<bool> SendMessageAsync(int chatId, Message message)
        {
            var chat = await _chats.FindAsync(chatId);
            if (message != null)
            {
                chat.Messages.Add(message);
                _messages.Add(message);
                _chats.Update(chat);
                var result = await _context.SaveChangesAsync();
                if (result == 1)
                {
                    return true;
                }
            }
            return false;
        }
    }
}
