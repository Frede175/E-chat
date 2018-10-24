using System.ComponentModel.DataAnnotations.Schema;
using Server.Context;

namespace Server.DbModels
{
    public class UserChat
    {
        
        [ForeignKey("ApplicationUser")]
        public string UserId {get; set;}

        public virtual ApplicationUser ApplicationUser {get; set; }

        [ForeignKey("Chat")]
        public int ChatId {get; set;}

        public virtual Chat Chat {get; set;}

    }
}