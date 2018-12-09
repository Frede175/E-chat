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

            //Create roles:

            var adminRole = await roleManager.FindByNameAsync("admin");
            var leaderRole = await roleManager.FindByNameAsync("leader");
            var normalUserRole = await roleManager.FindByNameAsync("normal");


            if (adminRole == null) {
                adminRole = new IdentityRole("admin");
                var result = await roleManager.CreateAsync(adminRole);
                if (!result.Succeeded) return;
                await AddAllPermissionsToRole(adminRole, roleManager);
            }
            
            if (leaderRole == null) {
                leaderRole = new IdentityRole("leader");
                var result = await roleManager.CreateAsync(leaderRole);
                if (!result.Succeeded) return;
                await AddPermissionsToRole(roleManager, leaderRole,
                    Permission.BasicPermissions,
                    Permission.AddUserToChat,
                    Permission.RemoveUserFromChat,
                    Permission.CreateChat,
                    Permission.RemoveChat,
                    Permission.SeeLogs

                );
            }

            if (normalUserRole == null) {
                normalUserRole = new IdentityRole("normal");
                var result = await roleManager.CreateAsync(normalUserRole);
                if (!result.Succeeded) return;
                await AddPermissionsToRole(roleManager, normalUserRole, Permission.BasicPermissions);
            }
            

            //Get admin user
            var admin = await userManager.FindByNameAsync("admin");
            var leader = await userManager.FindByNameAsync("Lars");
            var normal = await userManager.FindByNameAsync("Kristian");


            if (admin == null) {
                admin = await CreateUser(userManager, roleManager, "admin", "AdminAdmin123*", adminRole.Name);
            }

            if (leader == null) {
                leader = await CreateUser(userManager,roleManager, "Lars", "LarsLars123*", leaderRole.Name);
            }

            if (normal == null) {
                normal = await CreateUser(userManager, roleManager, "Kristian", "Kristian123*", normalUserRole.Name);
            }
            

            //Create department:
            var department = await departmentService.GetSpecificDepartmentAsync("Main");
            
            if (department == null) {
                department = await departmentService.CreateDepartmentAsync(new Department() {
                    Name = "Main"
                });
                if (department == null) return;
                await departmentService.AddUsersToDepartmentAsync(department.Id, admin, leader, normal);
            }


            var chat = await chatService.GetSpecificChat(department.Id, "Main");

            if (chat == null) {
                chat = await chatService.CreateChatAsync(new Chat() {
                    IsGroupChat = true,
                    DepartmentId = department.Id,
                    Name = "Main"
                }, admin.Id);
                if (chat == null) return;
                await chatService.AddUsersToChatAsync(chat.Id, normal.Id, leader.Id);
            }

        }

        private static async Task<ApplicationUser> CreateUser(UserManager<ApplicationUser> userManager, RoleManager<IdentityRole> roleManager, string userName, string password, string role) {
            var user = new ApplicationUser {
                UserName = userName
            };
            var result = await userManager.CreateAsync(user, password);
            if (result.Succeeded) {
                await userManager.AddToRoleAsync(user, role);
                return user;
            }
            return null;
        }

        private static async Task AddPermissionsToRole(RoleManager<IdentityRole> roleManager, IdentityRole role, params Permission[] permissions) {
            var claims = (await roleManager.GetClaimsAsync(role)).ToList();

            foreach (var permission in permissions) {
                if (!claims.Any(c => c.Type == UserClaimTypes.Permission && c.Value == permission.ToString())) {  
                    await roleManager.AddClaimAsync(role, new Claim(UserClaimTypes.Permission, permission.ToString()));
                }
            }
        }

        private static async Task AddAllPermissionsToRole(IdentityRole role, RoleManager<IdentityRole> roleManager) {
            var claims = (await roleManager.GetClaimsAsync(role)).ToList();

            foreach (var p in Enum.GetValues(typeof(Permission))) {
                if (!claims.Any(c => c.Type == UserClaimTypes.Permission && c.Value == p.ToString())) {
                    await roleManager.AddClaimAsync(role, new Claim(UserClaimTypes.Permission, p.ToString()));
                }
            }
        }
    }
}