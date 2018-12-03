using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Server.Context;
using Server.Logging;
using Server.Security;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class PermissionController : Controller
    {
        private readonly ILogger<PermissionController> _logger;

        private readonly UserManager<ApplicationUser> _userManager;

        public PermissionController(ILogger<PermissionController> logger, UserManager<ApplicationUser> userManager)
        {
            _logger = logger;
            _userManager = userManager;
        }

        // GET: https://localhost:5001/api/permission
        [HttpGet]
        public ActionResult<IEnumerable<string>> GetPermissions() 
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting all permissions.", username);
            return Ok(Enum.GetNames(typeof(Permission)));
        }
    }
}