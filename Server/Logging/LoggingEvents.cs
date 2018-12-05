using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class LoggingEvents
    {

        public const int ListItems = 1000;
        public const int GetItem = 1001;
        public const int UpdateItem = 1002;
        public const int DeleteItem = 1003;
        public const int UpdateRelativeItem = 1004;
        public const int InsertItem = 1005;


        public const int ListItemsNotFound = 1400;
        public const int GetItemNotFound = 1401;
        public const int UpdateItemNotFound = 1402;
        public const int DeleteItemNotFound = 1403;
        public const int UpdateRelativeItemNotFound = 1404;
        public const int InsertItemNotFound = 1405;


        public const int ListItemsFail = 1500;
        public const int UpdateItemFail = 1502;
        public const int DeleteItemFail = 1503;
        public const int UpdateRelativeItemFail = 1504;
        public const int InsertItemFail = 1505;


        public const int SendMessage = 2000;
        public const int ConnectedToHub = 2001;
        public const int DisconnectedFromHub = 2002;


        public const int AuthenticationFail = 3000;
        public const int AuthenticationSuccess = 3001;
        public const int Unauthorized = 3002;
        public const int SignOut = 3003;
        public const int AuthenticationStarted = 3004;
        
    }
}