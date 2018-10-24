using System.ComponentModel.DataAnnotations.Schema;
using Server.Context;

namespace Server.DbModels
{
    public class UserDepartment
    {

        public string UserId {get; set;}

        public virtual ApplicationUser ApplicationUser {get; set; }

        public int DepartmentId {get; set;}

        public virtual Department Department {get; set;}



    }
}