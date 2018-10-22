using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;

namespace Server.Context
{
    //Not working atm.
    public static class ApplicationDbInitializer
    {
        public static async Task SeedUsersAsync(UserManager<ApplicationUser> userManager) {

            var user = await userManager.FindByNameAsync("admin");

            if (user == null) {
                await userManager.CreateAsync(new ApplicationUser() {
                    UserName = "admin",
                    Email = "fsr1998@hotmail.com",
                    EmailConfirmed = true
                }, "AdminAdmin123*");
            }
        }
    }
}