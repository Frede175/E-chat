using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using OpenIddict.Validation;
using Microsoft.AspNetCore.Http;

namespace Server.Hubs
{
    [Authorize(AuthenticationSchemes = OpenIddictValidationDefaults.AuthenticationScheme)]
    public class ChatHub : Hub {

        public async Task SendMessage(MessageObject message)
        {
            message.User = Context.User.Identity.Name;
            await Clients.All.SendAsync("ReceiveMessage", message);
        }
    }


    public class MessageObject {
        public string User {get;set;}
        public string Message {get;set;}
    }
}