using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;

namespace Server.Hubs
{
    public interface IHubState<T> where T : Hub
    {
        Dictionary<string, List<string>> Connections {get;}
        Task AddUserToGroupAsync(IHubContext<T> context, string userId, string group);
        Task RemoveUserFromGroupAsync(IHubContext<T> context, string userId, string group);
    }
}