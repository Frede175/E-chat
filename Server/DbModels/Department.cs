using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Server.Context;

namespace Server.Models
{
    public class Department
    {
        [Key]
        public int Id { get; set; }

        public string Name { get; set; }

        public virtual ICollection<ApplicationUser> ApplicationUsers { get; set; }

        public virtual ICollection<Chat> Chats { get; set; }
    }
}