using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class LoggingEvents
    {
        public const int Get = 1000;
        public const int Post = 1001;
        public const int Put = 1002;
        public const int Delete = 1003;

        public const int NotFound = 1100;
        
        public const int SendMessage = 2000;
        public const int ConnectedToHub = 2001;
        public const int DisconnectedFromHub = 2002;
        public const int AuthenticationFail = 3000;
        public const int AuthenticationSuccess = 3001;
        public const int Unauthorized = 3002;
        public const int SignOut = 3003;
    }
}