using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Server.Context;
using Server.Models;

namespace Server.Service.Interfaces
{
    public interface IChatService
    {
        Task<bool> ChatIsActive(int chatId);

        Task<bool> SendMessage(Message message);

        Task<bool> CreateChat(Chat chat);

        Task<bool> RemoveChat(int chatId);

        Task<bool> AddUsersToChat(int id, params string[] userId);

        Task<bool> RemoveUsersFromChat(int id, params string[] userId);

        Task<bool> InviteToChat(int chatId, string userId);

        Task<ICollection<Message>> RetrieveMessages(int chatId);

        Task<ICollection<Chat>> RetrieveChats(string userId);

    }
}
