using Microsoft.AspNetCore.Identity;

namespace Server.Models
{
    public class Role
    {

        public string Id { get; set; }
        public string Name { get; set; }

        public Role()
        {

        }

        public Role(IdentityRole role)
        {
            Id = role.Id;
            Name = role.Name;
        }
    }
}