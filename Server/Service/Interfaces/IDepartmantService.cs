using Server.Context;
using Server.DbModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Server.Service.Interfaces
{
    public interface IDepartmentService
    {
        ApplicationDbContext GetContext();
        Task<Department> CreateDepartmentAsync(Department department);

        Task<bool> RemoveDepartmentAsync(int id);

        Task<bool> UpdateDepartmentAsync(Department department);

        Task<bool> AddUsersToDepartmentAsync(int departmentId, params ApplicationUser[] users);

        Task<bool> RemoveUsersFromDepartmentAsync(int departmentId, params ApplicationUser[] users);
        
        Task<List<Department>> GetDepartmentsAsync();

        Task<List<Department>> GetDepartmentsAsync(string userId);

        Task<Department> GetSpecificDepartmentAsync(string name);

        Task<Department> GetSpecificDepartmentAsync(int depId);

        Task<List<ApplicationUser>> GetUsersInDepartmentsAsync(params int[] ids);

        Task<List<Department>> GetAvailableDepartmentsAsync(string userId);

    }
}