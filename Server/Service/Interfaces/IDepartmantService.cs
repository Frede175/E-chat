using Server.Context;
using Server.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Server.Service.Interfaces
{
    public interface IDepartmentService
    {
        Task<bool> CreateDepartmentAsync(Department department);

        Task<bool> RemoveDepartmentASync(int id);

        Task<bool> UpdateDepartmentAsync(Department department);

        Task<bool> AddUsersToDepartmentAsync(int departmentId, params ApplicationUser[] users);

        Task<bool> RemoveUsersFromDepartmentAsync(int departmentId, params ApplicationUser[] users);
        
        Task<List<Department>> GetDepartmentsAsync();

        Task<List<Department>> GetDepartmentsAsync(ApplicationUser user);


    }
}