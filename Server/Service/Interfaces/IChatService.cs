﻿using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Server.Context;
using Server.DbModels;

namespace Server.Service.Interfaces
{
    public interface IChatService
    {
        Task<bool> ChatIsActiveAsync(int chatId);

        Task<Chat> CreateChatAsync(Chat chat, string userId);

        Task<bool> RemoveChatsAsync(params Chat[] chats);

        Task<bool> AddUsersToChatAsync(int chatId, params string[] usersIds);

        Task<bool> RemoveUsersFromChatAsync(int chatId, params string[] usersIds);

        Task<bool> InviteToChatAsync(int chatId, ApplicationUser user);

        Task<ICollection<Message>> RetrieveMessagesAsync(int chatId, int page, int pageSize);

        Task<ICollection<Chat>> GetChatsAsync(string userId, int departmentId);

        Task<ICollection<Chat>> GetChatsAsync(string userId);

        Task<ICollection<Chat>> GetChatsAsync();

        Task<Chat> GetSpecificChat(int chatId);

        Task<Chat> GetSpecificChat(int depId, string chatName);

        Task<List<ApplicationUser>> GetUsersInChat(int chatId);

        Task<bool> PrivateChatExists(string userId, string userId2);

        Task<Chat> CreatePrivateChat(Chat chat, string userId, string userId2);
        Task<List<Chat>> GetPrivateChatsAsync(string userId);
        Task<List<Chat>> GetAvailableChatsAsync(string userId);
    }
}
