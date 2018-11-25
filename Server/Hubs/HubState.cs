using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;

namespace Server.Hubs
{
    public class HubState<T,T1> : IHubState<T,T1> where T : Hub<T1> where T1 : class
        { 
        public Dictionary<string, List<string>> Connections { get; }

        public HubState() {
            Connections = new Dictionary<string, List<string>>();
        }

        public async Task AddUserToGroupAsync(IHubContext<T,T1> context, string userId, string group) {
            if (Connections.TryGetValue(userId, out var userConnections)) {
                foreach (var connectionId in userConnections) {
                    await context.Groups.AddToGroupAsync(connectionId, group);
                }
            }
        }

        public async Task RemoveUserFromGroupAsync(IHubContext<T,T1> context, string userId, string group) {
            if (Connections.TryGetValue(userId, out var userConnections)) {
                foreach (var connectionId in userConnections) {
                    await context.Groups.RemoveFromGroupAsync(connectionId, group);
                }
            }
        }
    }
}