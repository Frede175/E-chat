using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Server.Context;

namespace Server.DbModels
{
    public class Message
    {
        [Key]
        public int Id { get; set; }

        public DateTime TimeStamp { get; set; }

        public string Content { get; set; }



        [ForeignKey("ApplicationUser")]
        public string SenderId { get; set; }

        [ForeignKey("Chat")]
        public int ChatId {get;set;}

        public virtual Chat Chat {get;set;}

        public virtual ApplicationUser ApplicationUser { get; set; }
    }
}