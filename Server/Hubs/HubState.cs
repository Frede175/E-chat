using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;

namespace Server.Hubs
{
    public class HubState<T> : IHubState<T> where T : Hub
    { 
        public Dictionary<string, List<string>> Connections { get; }

        public HubState() {
            Connections = new Dictionary<string, List<string>>();
        }

        public async Task AddUserToGroupAsync(IHubContext<T> context, string userId, string group) {
            if (Connections.TryGetValue(userId, out var userConnections)) {
                foreach (var connectionId in userConnections) {
                    await context.Groups.AddToGroupAsync(connectionId, group);
                }
            }
        }

        public async Task RemoveUserFromGroupAsync(IHubContext<T> context, string userId, string group) {
            if (Connections.TryGetValue(userId, out var userConnections)) {
                foreach (var connectionId in userConnections) {
                    await context.Groups.RemoveFromGroupAsync(connectionId, group);
                }
            }
        }
    }
}