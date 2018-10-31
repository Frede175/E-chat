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
        [RequiresPermissionAttribute(Permission.GetUserDepartments)]
        public async Task<ActionResult<ICollection<User>>> GetUsers()
        {
            return await _userManager.Users.Select(u => new User(u)).ToListAsync();
        }


        // GET: https://localhost:5001/api/User/{userId}
        [HttpGet("{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetUserDepartments)]
        public async Task<ActionResult<ICollection<User>>> GetContacts(string userId)
        {
            var deps = await _departmentService.GetDepartmentsAsync(userId);

            var x = deps.SelectMany(d => d.UserDepartments.Select(u => new User(u.ApplicationUser)));

            return x.ToList();
        }
    }
}
