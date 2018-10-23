using Server.Context;
using Server.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Server.Service.Interfaces
{
    public interface IDepartmentService
    {
        Task<bool> CreateDepartmentAsync(Department department);

        Task<bool> RemoveDepartmentSync(int Id);

        void UpdateDepartment(Department department);

        void AddUserToDepartment(int departmentId, ApplicationUser user);

        void AddUsersToDepartment(int departmentId, params ApplicationUser[] users);

        void RemoveUserFromDepartment(int departmentId, ApplicationUser user);

        void RemoveUsersFromDepartment(int departmentId, params ApplicationUser[] user);
        
        List<Department> GetDepartments();


    }
}