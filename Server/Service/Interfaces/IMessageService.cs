using System.Threading.Tasks;
using Server.Context;
using Server.DbModels;

namespace Server.Service.Interfaces
{
    public interface IMessageService
    {
        Task<Message> SendMessageAsync(int chatId, string userId, string content);

        Task<Message> SendUpdateMessageAsync(int chatId, ApplicationUser user, UpdateMessageType type);

        Task<Message> SendUpdateMessageAsync(int chatId, string userId, UpdateMessageType type);
    }



    public enum UpdateMessageType 
    {
        ADD,
        REMOVE,
        LEAVE
    }
}