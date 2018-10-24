using System.ComponentModel.DataAnnotations.Schema;
using Server.Context;

namespace Server.DbModels
{
    public class UserChat
    {
        
        public string UserId {get; set;}

        public virtual ApplicationUser ApplicationUser {get; set; }

        public int ChatId {get; set;}

        public virtual Chat Chat {get; set;}

    }
}