using System.Threading.Tasks;
using Server.Models;

namespace Server.Hubs
{
    public interface IChatHub
    {
         Task ReceiveMessage(Message message);

         Task Add(int chatId, User user);

         Task Remove(int chatId, User user);

         Task Leave(int chatId, User user);

         Task NewChat(Chat chat);
    }
}