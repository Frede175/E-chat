using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;

namespace Server.Hubs
{
    public interface IHubState<T, T1> where T : Hub<T1> where T1 : class
    {
        Dictionary<string, List<string>> Connections {get;}
        Task AddUserToGroupAsync(IHubContext<T,T1> context, string userId, string group);
        Task RemoveUserFromGroupAsync(IHubContext<T,T1> context, string userId, string group);
    }
}