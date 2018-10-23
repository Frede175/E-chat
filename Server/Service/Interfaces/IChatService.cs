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

        Task<bool> SendMessageAsync(int chatId, Message message);

        Task<bool> CreateChatAsync(Chat chat);

        Task<bool> RemoveChatAsync(int chatId, ApplicationUser user);

        Task<bool> AddUsersToChatAsync(int chatId, params ApplicationUser[] users);

        Task<bool> RemoveUsersFromChatAsync(int chatId, params ApplicationUser[] users);

        Task<bool> InviteToChatAsync(int chatId, ApplicationUser user);

        Task<ICollection<Message>> RetrieveMessagesAsync(int chatId);

        Task<ICollection<Chat>> RetrieveChatsAsync(ApplicationUser user);

    }
}
