using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class DbLogger : ILogger
    {
        private readonly LogLevel _logLevel;

        private readonly IDbLoggingHandler _loggingHandler;

        public DbLogger(LogLevel logLevel, IDbLoggingHandler loggingHandler) {
            _logLevel = logLevel;
            _loggingHandler = loggingHandler;
        }

        public IDisposable BeginScope<TState>(TState state)
        {
            return null;
        }

        public bool IsEnabled(LogLevel logLevel)
        {
            return logLevel >= _logLevel;
        }

        public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter)
        {
            var message = new LogMessage() {
                EventId = eventId,
                LogLevel = logLevel,
                Message = formatter(state, exception)
            };

            _loggingHandler.AddToQueue(message);


        }
    }
}