namespace Server.Models
{
    public class CreateUser
    {
        public string UserName { get; set; }
        public string Password { get; set; }

        public string Role {get;set;}
    }
}