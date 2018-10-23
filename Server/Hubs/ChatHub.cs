using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using OpenIddict.Validation;
using Microsoft.AspNetCore.Http;
using Server.Context;

namespace Server.Hubs
{
    [Authorize(AuthenticationSchemes = OpenIddictValidationDefaults.AuthenticationScheme)]
    public class ChatHub : Hub
    {

        private readonly UserManager<ApplicationUser> _userManager;
        
        public ChatHub(UserManager<ApplicationUser> userManager) {
            _userManager = userManager;
        }

        public async Task GetUser() {
            var user = _userManager.GetUserAsync(Context.User);
            await Clients.Caller.SendAsync("Users", user);
        }


        public async Task SendMessage(MessageObject message)
        {

            message.User = Context.User.Identity.Name;
            await Clients.All.SendAsync("ReceiveMessage", message);
        }
    }


    public class MessageObject
    {
        public string User { get; set; }
        public string Message { get; set; }
    }
}