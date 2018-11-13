using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.DependencyInjection;
using Server.DbModels;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Context
{
    //Not working atm.
    public static class ApplicationDbInitializer
    {
        public static async Task SeedUsersAsync(IServiceProvider services) {

            var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
            var roleManager = services.GetRequiredService<RoleManager<IdentityRole>>();
            var departmentService = services.GetRequiredService<IDepartmentService>();
            var chatService = services.GetRequiredService<IChatService>();

            //Create role:

            var role = await roleManager.FindByNameAsync("admin");
            if (role == null) {
                role = new IdentityRole("admin");
                var result = await roleManager.CreateAsync(role);
                if (!result.Succeeded) return;
            }


            await AddAllPermissionsToRole(role, roleManager);

            //Get admin user
            var user = await userManager.FindByNameAsync("admin");


            if (user == null) {
                user = await CreateUser(userManager, "admin", "AdminAdmin123*");

            }

            await userManager.AddToRoleAsync(user, role.Name);

            //Create department:
            var department = await departmentService.GetSpecificDepartment("Main");
            
            if (department == null) {
                var result = await departmentService.CreateDepartmentAsync(new Department() {
                    Name = "Main"
                });
                if (result == null) return;
                department = await departmentService.GetSpecificDepartment("Main");
                await departmentService.AddUsersToDepartmentAsync(department.Id, user);
            }


            var chat = await chatService.GetSpecificChat(department.Id, "Main");

            if (chat == null) {
                chat = await chatService.CreateChatAsync(new Chat() {
                    DepartmentId = department.Id,
                    Name = "Main"
                }, user.Id);
                if (chat == null) return;
                //chat = await chatService.GetSpecificChat(department.Id, "Main");
            }
            
        }

        private static async Task<ApplicationUser> CreateUser(UserManager<ApplicationUser> userManager, string userName, string password) {
            var user = new ApplicationUser {
                UserName = userName
            };
            var result = await userManager.CreateAsync(user, password);
            if (result.Succeeded) return user;
            return null;
        }


        private static async Task AddAllPermissions(ApplicationUser user, UserManager<ApplicationUser> userManager) {
            var claims = (await userManager.GetClaimsAsync(user)).ToList();

             var newClaims = new List<Claim>();

            foreach (var p in Enum.GetValues(typeof(Permission))) {
                if (!claims.Any(c => c.Type == UserClaimTypes.Permission && c.Value == p.ToString())) {
                    newClaims.Add(new Claim(UserClaimTypes.Permission, p.ToString()));
                }
            }

            await userManager.AddClaimsAsync(user, newClaims);

        }

        private static async Task AddAllPermissionsToRole(IdentityRole role, RoleManager<IdentityRole> roleManager) {
            var claims = (await roleManager.GetClaimsAsync(role)).ToList();

             var newClaims = new List<Claim>();

            foreach (var p in Enum.GetValues(typeof(Permission))) {
                if (!claims.Any(c => c.Type == UserClaimTypes.Permission && c.Value == p.ToString())) {
                    newClaims.Add(new Claim(UserClaimTypes.Permission, p.ToString()));
                }
            }

            foreach(var claim in newClaims) {
                await roleManager.AddClaimAsync(role, claim);
            }

        }
    }
}