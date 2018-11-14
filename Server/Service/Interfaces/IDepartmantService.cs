using Server.Context;
using Server.DbModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Server.Service.Interfaces
{
    public interface IDepartmentService
    {
        Task<Department> CreateDepartmentAsync(Department department);

        Task<bool> RemoveDepartmentASync(int id);

        Task<bool> UpdateDepartmentAsync(Department department);

        Task<bool> AddUsersToDepartmentAsync(int departmentId, params ApplicationUser[] users);

        Task<bool> RemoveUsersFromDepartmentAsync(int departmentId, params ApplicationUser[] users);
        
        Task<List<Department>> GetDepartmentsAsync();

        Task<List<Department>> GetDepartmentsAsync(string userId);

        Task<Department> GetSpecificDepartment(string name);

        Task<Department> GetSpecificDepartment(int depId);

        Task<List<ApplicationUser>> GetUsersInDepartments(params int[] ids);

    }
}