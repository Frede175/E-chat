using System;
using System.Diagnostics;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class DbLoggingProvider<T> : ILoggerProvider where T : DbContext
    {
        private readonly Func<string, LogLevel, bool> _filter;
        private readonly IServiceProvider _serviceProvider;

        private IDbLoggingHandler _handler;

        private IDbLoggingHandler GetHandler
        {
            get
            {
                if (_handler == null)
                {
                    _handler = _serviceProvider.GetRequiredService<IDbLoggingHandler>();
                }
                return _handler;
            }
        }

        public DbLoggingProvider(IServiceProvider provider, Func<string, LogLevel, bool> filter)
        {
            _filter = filter;
            _serviceProvider = provider;
        }

        public ILogger CreateLogger(string categoryName)
        {
            return new DbLogger(GetHandler, categoryName, _filter);
        }

        public void Dispose()
        {

        }
    }
}