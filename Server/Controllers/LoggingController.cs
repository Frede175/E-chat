using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Server.Context;
using Server.Logging;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class LoggingController : Controller
    {
        private readonly ILoggingService _loggingService;

        private readonly ILogger<LoggingController> _logger;

        private readonly UserManager<ApplicationUser> _userManager;

        public LoggingController(ILoggingService loggingService, ILogger<LoggingController> logger, UserManager<ApplicationUser> userManager) 
        {
            _loggingService = loggingService;
            _logger = logger;
            _userManager = userManager;
        }

        // GET: https://localhost:5001/api/logging/custom
        [HttpGet("custom")]
        [RequiresPermissionAttribute(permissions: Permission.SeeLogs)]
        public async Task<ActionResult> GetCustomLogs([FromBody] Page page)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting custom logs", username);

            if (!ModelState.IsValid)
            {
                _logger.LogWarning(LoggingEvents.ListItemsFail, "{username} failed getting custom logs, Model state is not valid.", username);
                return BadRequest(ModelState);
            }

            return Ok(await _loggingService.GetLogMessagesAsync(page.PageNumber, page.PageSize));
        }

        // GET: https://localhost:5001/api/logging
        [HttpGet]
        [RequiresPermissionAttribute(permissions: Permission.SeeAllLogs)]
        public async Task<ActionResult> GetLogs([FromBody] Page page)
        {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.ListItems, "{username} getting all logs", username);

            if (!ModelState.IsValid)
            {
                _logger.LogWarning(LoggingEvents.ListItemsFail, "{username} failed getting all logs, Model state is not valid.", username);
                return BadRequest(ModelState);
            }

            return Ok(await _loggingService.GetLogMessagesAsync(page.PageNumber, page.PageSize, true));
        }
    }
}