using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Server.Security;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class PermissionController : Controller
    {
        // GET: https://localhost:5001/api/permission
        [HttpGet]
        public ActionResult<IEnumerable<string>> GetPermissions() 
        {
            return Ok(Enum.GetNames(typeof(Permission)));
        }
    }
}