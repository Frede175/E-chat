using Server.Context;

namespace Server.Models
{
    public class User
    {
        public User() {}

        public User(ApplicationUser user) {
            Id = user.Id;
            Name = user.UserName;
        }

        public string Id { get; set; }

        public string Name { get; set; }
    }
}