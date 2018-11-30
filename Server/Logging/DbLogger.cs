using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class DbLogger : ILogger
    {
        private readonly string _category;

        private readonly Func<string, LogLevel, bool> _filter;

        private readonly IDbLoggingHandler _handler;

        public DbLogger(IDbLoggingHandler handler, string category, Func<string, LogLevel, bool> filter) {
            _category = category;
            _filter = filter;
            _handler = handler;
        }

        public IDisposable BeginScope<TState>(TState state)
        {
            return null;
        }

        public bool IsEnabled(LogLevel logLevel)
        {
            return _filter(_category, logLevel);
        }

        public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter)
        {
            if (!IsEnabled(logLevel)) return;

            var message = new LogMessage(eventId.Id, logLevel, formatter(state, exception), DateTime.UtcNow);

            _handler.Add(message);

            //_handler.Add(message);
        }
    }
}