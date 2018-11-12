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


        public async Task<bool> CreateDepartmentAsync(Department department)
        {
            _department.Add(department);
            var result = await _context.SaveChangesAsync();
            if (result == 1) return true;
            return false;
        }

        public async Task<List<Department>> GetDepartmentsAsync()
        {
            return await _department.Cast<Department>().ToListAsync();
        }

        public async Task<List<ApplicationUser>> GetContacts(string userId) {
            var de = _department.Cast<Department>().Where(d => d.UserDepartments.Any(u => u.UserId == userId));

            return await _userDepartment.Where(d => de.Select(i => i.Id).Contains(d.DepartmentId)).Select(a => a.ApplicationUser).ToListAsync();
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
                _department.Update(department);
                var result = await _context.SaveChangesAsync();
                if (result == 1) return true;
            }
            return false;
        }

        public async Task<bool> RemoveDepartmentASync(int id)
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
                    _userDepartment.Remove(new UserDepartment(){UserId = user.Id, DepartmentId = department.Id});
                }

                _department.Update(department);
                var result = await _context.SaveChangesAsync();
                if (result == 1) return true;
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

        public async Task<Department> GetSpecificDepartment(string name)
        {
            return await _department.Cast<Department>().SingleOrDefaultAsync(d => string.Equals(d.Name, name, StringComparison.InvariantCultureIgnoreCase));
        }

        public async Task<Department> GetSpecificDepartment(int depId)
        {
            return await _department.Cast<Department>().SingleOrDefaultAsync(d => d.Id.Equals(depId));
        }
    }
}