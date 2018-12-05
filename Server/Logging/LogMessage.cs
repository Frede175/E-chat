using System;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class LogMessage
    {

        public LogMessage() {}

        public LogMessage(int eventId, LogLevel logLevel, string message, DateTime timeStamp) 
        {
            EventId = eventId;
            LogLevel = logLevel;
            Message = message;
            TimeStamp = timeStamp;
        } 


        public int Id { get; set; }

        public string Message { get; set; }
        public int EventId { get; set; }

        public LogLevel LogLevel { get; set; }

        public DateTime TimeStamp { get; set; }

        public override string ToString()
        {
            return $"LogMessage ({LogLevel}): {Message}";
        }
    }
}