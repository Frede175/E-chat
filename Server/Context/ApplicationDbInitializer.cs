using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.DependencyInjection;

namespace Server.Context
{
    //Not working atm.
    public static class ApplicationDbInitializer
    {
        public static async Task SeedUsersAsync(IServiceProvider services) {

            var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
            var roleManager = services.GetRequiredService<RoleManager<IdentityRole>>();
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