using Microsoft.EntityFrameworkCore;

namespace Server.Logging
{
    public interface IDbLoggingHandler
    {
         void AddToQueue(LogMessage message);
    }
}