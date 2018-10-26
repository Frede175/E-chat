using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Server.Context;
using Server.Models;
using Server.Security;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [Authorize]
    [ApiController]
    public class DepartmentController : Controller
    {

        private readonly IDepartmentService _departmentService;
        private readonly UserManager<ApplicationUser> _userManager;

        public DepartmentController(IDepartmentService departmentService, UserManager<ApplicationUser> userManager)
        {
            _departmentService = departmentService;
            _userManager = userManager;

        }

        // GET: https://localhost:5001/api/Department
        [HttpGet, Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetDepartments)]
        public async Task<ActionResult<ICollection<Department>>> GetDepartments()
        {
            return (await _departmentService.GetDepartmentsAsync()).Select(d => new Department(d)).ToList();

        }

        // GET: https://localhost:5001/api/Department/{id} 
        [HttpGet("{userId}"), Produces("application/json")]
        [RequiresPermissionAttribute(Permission.GetDepartment)]
        public async Task<ActionResult<ICollection<Department>>> GetDepartments(string userId)
        {
            return (await _departmentService.GetDepartmentsAsync(userId)).Select(d => new Department(d)).ToList();
        }

        [HttpPost]
        [RequiresPermissionAttribute(Permission.CreateDepartment)]
        public async Task<IActionResult> CreateDepartment(Department department)
        {
            var d = new DbModels.Department()
            {
                Name = department.Name
            };
            var result = await _departmentService.CreateDepartmentAsync(d);
            if (result)
            {
                return new StatusCodeResult(201);
            }
            return new BadRequestResult();
        }

        // POST: https://localhost:5001/api/Department/{departmentId}
        [HttpPost("{departmentId}")]
        [RequiresPermissionAttribute(Permission.AddUserToDepartment)]
        public async Task<IActionResult> AddUserToDepartment(int departmentId, string userId)
        {
            var result = await _departmentService.AddUsersToDepartmentAsync(departmentId, await _userManager.FindByIdAsync(userId));
            if (result)
            {
                return new OkResult();
            }
            return new BadRequestResult();

        }


        // DELETE: https://localhost:5001/api/Department/remove/{departmentId}
        [HttpDelete("remove/{departmentId}")]
        [RequiresPermissionAttribute(Permission.DeleteDepartment)]
        public async Task<ActionResult> RemoveDepartment(int departmentId)
        {
            var result = await _departmentService.RemoveDepartmentASync(departmentId);
            if (result)
            {
                return new OkResult();
            }
            return new BadRequestResult();
        }

    }
}