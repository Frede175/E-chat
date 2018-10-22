using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.Models;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class DepartmentService : IDepartmentService
    {

        private readonly DbSet<Department> _department;

        private readonly ApplicationDbContext _context;

        public DepartmentService(ApplicationDbContext context) {
            _department = context.Set<Department>();
            _context = context;
        }


        public async Task<bool> AddDepartmentAsync(Department department)
        {
            _department.Add(department);
            await _context.SaveChangesAsync();
            return true;
        }

        public List<Department> GetDepartments() {
            return _department.Cast<Department>().ToList();
        }

        public void AddUsersToDepartment(int departmentId, params ApplicationUser[] users)
        {
            throw new System.NotImplementedException();
        }

        public void AddUserToDepartment(int departmentId, ApplicationUser user)
        {
            throw new System.NotImplementedException();
        }

        public async Task<bool> RemoveDepartmentSync(int Id)
        {
            var department = await _department.FindAsync(Id);
            if (department != null) {
                _department.Remove(department);
                await _context.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public void RemoveUserFromDepartment(int departmentId, ApplicationUser user)
        {
            throw new System.NotImplementedException();
        }

        public void RemoveUsersFromDepartment(int departmentId, params ApplicationUser[] user)
        {
            throw new System.NotImplementedException();
        }

        public void UpdateDepartment(Department department)
        {
            throw new System.NotImplementedException();
        }

    }
}