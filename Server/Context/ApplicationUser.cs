using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;
using Server.DbModels;

namespace Server.Context {
    public class ApplicationUser : IdentityUser{
        public virtual ICollection<UserDepartment> UserDepartments {get;set;}

        public virtual ICollection<UserChat> UserChats {get;set;}

        public virtual ICollection<Message> Messages {get;set;}
    }
}