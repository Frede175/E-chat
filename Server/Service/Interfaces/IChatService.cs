using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Server.Context;
using Server.DbModels;

namespace Server.Service.Interfaces
{
    public interface IChatService
    {
        Task<bool> ChatIsActiveAsync(int chatId);

        Task<Message> SendMessageAsync(int chatId, string userId, string content);

        Task<bool> CreateChatAsync(Chat chat);

        Task<bool> RemoveChatAsync(int chatId, ApplicationUser user);

        Task<bool> AddUsersToChatAsync(int chatId, params string[] usersIds);

        Task<bool> RemoveUsersFromChatAsync(int chatId, params string[] usersIds);

        Task<bool> InviteToChatAsync(int chatId, ApplicationUser user);

        Task<ICollection<Message>> RetrieveMessagesAsync(int chatId);

        Task<ICollection<Chat>> RetrieveChatsAsync(string userId, int departmentId);

        Task<ICollection<Chat>> RetrieveChatsAsync(string userId);

    }
}
