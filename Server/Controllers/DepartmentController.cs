using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Server.Models;
using Server.Service.Interfaces;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DepartmentController
    {

        private readonly IDepartmentService _departmentService;

        public DepartmentController(IDepartmentService departmentService) {
            _departmentService = departmentService;
        }

        // GET: https://localhost:5001/api/Department
        [HttpGet, Produces("application/json")]
        public async Task<ActionResult<ICollection<Department>>> GetDepartments() {
            return (await _departmentService.GetDepartmentsAsync()).Select(d => new Department(d)).ToList(); 

        }

        // GET: https://localhost:5001/api/Department/{id} 
        [HttpGet("{id}"), Produces("application/json")]
        public async Task<ActionResult<ICollection<Department>>> GetDepartments(string userId) {
            return (await _departmentService.GetDepartmentsAsync(userId)).Select(d => new Department(d)).ToList(); 
        }

        [HttpPost]
        public IActionResult CreateDepartment(Department department) {
            return null;
        }
    }
}