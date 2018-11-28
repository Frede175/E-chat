using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class LogMessage
    {
        public int Id {get;set;}

        public string Message {get;set;}
        public EventId EventId {get;set;}

        public LogLevel LogLevel {get;set;}

        public override string ToString() 
        {
            return $"LogMessage ({LogLevel}): {Message}";
        }
    }
}