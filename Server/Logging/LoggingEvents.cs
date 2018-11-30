using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class LoggingEvents
    {
        public const int CreateGroupChat = 1000;

        public const int CreatePrivateChat = 1001;
        public const int DeleteChat = 1002;
        public const int SendMessage = 2000;
        public const int ConnectedToHub = 2001;
        public const int DisconnectedFromHub = 2002;
    }
}