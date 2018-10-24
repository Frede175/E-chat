using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Server.Context;

namespace Server.DbModels
{
    public class Department
    {
        [Key]
        public int Id { get; set; }

        public string Name { get; set; }

        public virtual ICollection<UserDepartment> UserDepartments { get; set; }

        public virtual ICollection<Chat> Chats { get; set; }
    }
}