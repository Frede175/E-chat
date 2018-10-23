using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;
using Server.Models;

namespace Server.Context {
    public class ApplicationUser : IdentityUser{
        public virtual ICollection<Department> Departments {get;set;}

        public virtual ICollection<Chat> Chats {get;set;}
    }
}