using System.Collections.Generic;
using System.Threading.Tasks;
using Server.Logging;

namespace Server.Service.Interfaces
{
    public interface ILoggingService
    {
         Task<List<LogMessage>> GetLogMessagesAsync(int page, int pageSize, bool all = false);
    }
}