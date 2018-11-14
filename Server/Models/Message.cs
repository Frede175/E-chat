using System;
using System.Collections.Generic;
using Server.Context;

namespace Server.Models
{
    public class Message
    {
        public int Id { get; set; }

        public int ChatId { get; set; }

        public DateTime TimeStamp { get; set; }

        public string Content { get; set; }

        public User user { get; set; }

        public Message(){

        }


        public Message(DbModels.Message message){
            Id = message.Id;
            TimeStamp = message.TimeStamp;
            Content = message.Content;
            user = new User(message.ApplicationUser);
            ChatId = message.ChatId;
        }
    }
}