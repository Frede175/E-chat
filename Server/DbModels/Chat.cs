using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Server.Context;

namespace Server.DbModels
{
    public class Chat
    {
        [Key]
        public int Id { get; set; }

        public string Name { get; set; }

        [ForeignKey("Department")]
        public int DepartmentId { get; set; }

        public virtual ICollection<UserChat> UserChats { get; set; }

        public virtual ICollection<Message> Messages { get; set; }

        public virtual Department Department { get; set; }

    }
}