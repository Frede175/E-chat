using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Server.Context;
using Server.Logging;
using Server.Service.Interfaces;

namespace Server.Service
{
    public class LoggingService : ILoggingService
    {
        private const int MinCustomEventId = LoggingEvents.ListItems;

        private const int MaxCustomEventId = 5000;


        private readonly ApplicationDbContext _context;

        private readonly DbSet<LogMessage> _logMessages;

        public LoggingService(ApplicationDbContext context) 
        {
            _context = context;
            _logMessages = _context.Set<LogMessage>();
        }

        public async Task<List<LogMessage>> GetLogMessagesAsync(int page, int pageSize, bool all = false)
        {
            return await _logMessages.Where(l => all || (l.EventId >= MinCustomEventId && l.EventId <= MaxCustomEventId)).Skip(page * pageSize).Take(pageSize).OrderByDescending(l => l.TimeStamp).ToListAsync();
        }
    }
}