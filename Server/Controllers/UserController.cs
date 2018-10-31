using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{

    [Route("api/[controller]")]
    [Authorize]
    [ApiController]

    public class UserController : Controller
    {
        private readonly IChatService _chatService;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IDepartmentService _departmentService;

        public UserController (IDepartmentService departmentService, 
                               UserManager<ApplicationUser> userManager, 
                               IChatService chatService)
        {
            _departmentService = departmentService;
            _userManager = userManager;
            _chatService = chatService;
        }


        // GET: https://localhost:5001/api/User/ 
        [HttpGet("{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetUsers)]
        public async Task<ActionResult<ICollection<User>>> GetUsers()
        {
            return await _userManager.Users.Select(u => new User(u)).ToListAsync();
        }


        // GET: https://localhost:5001/api/User/{userId}
        [HttpGet("{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetContacts)]
        public async Task<ActionResult<ICollection<User>>> GetContacts(string userId)
        {
            var deps = await _departmentService.GetDepartmentsAsync(userId);

            var x = deps.SelectMany(d => d.UserDepartments.Select(u => new User(u.ApplicationUser)));

            return x.ToList();
        }

        // POST: https://localhost:5001/api/User/create
        [HttpPost]
        [RequiresPermissionAttribute(Permission.CreateUser)]
        public async Task<ActionResult> CreateUser(CreateUserModel model)
        {
            var user = await _userManager.FindByNameAsync(model.UserName);
            if (user == null)
            {

                var newUser = new ApplicationUser()
                {
                    UserName = model.UserName
                };

                var result = await _userManager.CreateAsync(newUser, model.Password);

                if (result.Succeeded)
                {
                    return Ok();
                }
                return BadRequest();
            }
            return BadRequest();
        }


        // DELETE: https://localhost:5001/api/User/create
        [HttpDelete]
        [RequiresPermissionAttribute(Permission.DeleteUser)]
        public async Task<ActionResult> CreateUser(string userId)
        {
            var user = await _userManager.FindByIdAsync(userId);

            if (user != null)
            {

                var result = await _userManager.DeleteAsync(user);

                if (result.Succeeded)
                {
                    return Ok();
                }
            }
            return BadRequest();
        }


        // PUT https://localhost:5001/api/User/{userId}
        [HttpPut]
        [RequiresPermissionAttribute(Permission.AddAdditionalRole)]
        public async Task<ActionResult> AddAdditionalRole(string userId, string role) 
        {

            var user = await _userManager.FindByIdAsync(userId);

            var result = await _userManager.AddToRoleAsync(user, role);

            if (result.Succeeded)
            {
                return Ok();
            }
            return BadRequest();
        }



    }
}