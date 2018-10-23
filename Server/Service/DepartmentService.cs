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

        private readonly ApplicationDbContext _context;

        public DepartmentService(ApplicationDbContext context)
        {
            _department = context.Set<Department>();
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

        public async Task<List<Department>> GetDepartmentsAsync(string userId)
        {
            return await _department.Cast<Department>().Where(d => d.ApplicationUsers.Any(u => u.Id == userId)).ToListAsync();
        }

        public async Task<bool> AddUsersToDepartmentAsync(int departmentId, params ApplicationUser[] users)
        {
            var department = await _department.FindAsync(departmentId);
            if (department != null)
            {
                foreach (var user in users)
                {
                    department.ApplicationUsers.Add(user);
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
                    department.ApplicationUsers.Remove(user);
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

    }
}