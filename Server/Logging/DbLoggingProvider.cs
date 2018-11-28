using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public class DbLoggingProvider<T> : ILoggerProvider where T : DbContext
    {
        private readonly IDbLoggingHandler loggingHandler;

        public DbLoggingProvider(IServiceProvider serviceProvider) 
        {
            var context = serviceProvider.GetRequiredService<T>();
            loggingHandler = new DbLoggingHandler<T>(context);
        }

        public ILogger CreateLogger(string categoryName)
        {
            return new DbLogger(LogLevel.Information, loggingHandler);
        }

        public void Dispose()
        {
            throw new System.NotImplementedException();
        }
    }
}