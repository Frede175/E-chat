namespace Server.Models
{
    public class Chat
    {

        public int Id { get; set; }

        public string Name { get; set; }

        public bool IsGroupChat { get; set; }

        public Chat(){

        }

        public Chat(DbModels.Chat chat)
        {
            Id = chat.Id;
            Name = chat.Name;
            IsGroupChat = chat.IsGroupChat;
        }
    }
}