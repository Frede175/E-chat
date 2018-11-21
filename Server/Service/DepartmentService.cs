using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.DbModels;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class DepartmentService : IDepartmentService
    {

        private readonly DbSet<Department> _department;
        private readonly DbSet<UserDepartment> _userDepartment;

        private readonly ApplicationDbContext _context;

        public DepartmentService(ApplicationDbContext context)
        {
            _department = context.Set<Department>();
            _userDepartment = context.Set<UserDepartment>();
            _context = context;
        }


        public async Task<Department> CreateDepartmentAsync(Department department)
        {
            _department.Add(department);
            var result = await _context.SaveChangesAsync();
            if (result == 1) return department;
            return null;
        }

        public async Task<List<Department>> GetDepartmentsAsync()
        {
            return await _department.Cast<Department>().ToListAsync();
        }

        public async Task<List<ApplicationUser>> GetUsersInDepartmentsAsync(params int[] ids) {
            return await _userDepartment.Cast<UserDepartment>().Where(i => ids.Contains(i.DepartmentId)).Select(d => d.ApplicationUser).ToListAsync();
        }

        public async Task<List<Department>> GetDepartmentsAsync(string userId)
        {
            return await _department.Cast<Department>().Where(d => d.UserDepartments.Any(u => u.UserId == userId)).ToListAsync();
        }

        public async Task<bool> AddUsersToDepartmentAsync(int departmentId, params ApplicationUser[] users)
        {
            var department = await _department.FindAsync(departmentId);
            if (department != null)
            {
                foreach (var user in users)
                {
                    _userDepartment.Add(new UserDepartment() { UserId = user.Id, DepartmentId = department.Id });
                }
                var result = await _context.SaveChangesAsync();
                if (result == users.Count()) return true;
            }
            return false;
        }

        public async Task<bool> RemoveDepartmentAsync(int id)
        {
            
            var department = await _department.FindAsync(id);
            if (department != null)
            {
                _department.Remove(department);
                var result = await _context.SaveChangesAsync();
                if (result == 1) return true;
            }
            return false;
        }

        public async Task<bool> RemoveUsersFromDepartmentAsync(int departmentId, params ApplicationUser[] users)
        {
            var department = await _department.FindAsync(departmentId);
            if (department != null)
            {
                foreach (var user in users)
                {
                    _userDepartment.Remove(new UserDepartment() {UserId = user.Id, DepartmentId = department.Id});
                }
                var result = await _context.SaveChangesAsync();
                if (result == users.Count()) return true;
            }
            return false;
        }

        public async Task<bool> UpdateDepartmentAsync(Department department)
        {
            var d = await _department.FindAsync(department.Id);
            if (d != null)
            {
                d.Name = department.Name;
                _department.Update(d);
                var result = await _context.SaveChangesAsync();
                if (result == 1) return true;
            }

            return false;
        }

        public async Task<Department> GetSpecificDepartmentAsync(string name)
        {
            return await _department.Cast<Department>().SingleOrDefaultAsync(d => string.Equals(d.Name, name, StringComparison.InvariantCultureIgnoreCase));
        }

        public async Task<Department> GetSpecificDepartmentAsync(int depId)
        {
            return await _department.Cast<Department>().SingleOrDefaultAsync(d => d.Id.Equals(depId));
        }
    }
}