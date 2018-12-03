using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public static class DbLoggingExtentions
    {
        public static ILoggingBuilder AddDbLogging<T>(this ILoggingBuilder builder) where T : DbContext
        {
            builder.Services.AddSingleton<IDbLoggingHandler, DbLoggingHandler<T>>();
            return builder;
        }


        public static void AddDbLogging<T>(this ILoggerFactory factory, IServiceProvider provider, Func<string, LogLevel, bool> filter) where T : DbContext
        {
            factory.AddProvider(new DbLoggingProvider<T>(provider, filter));
        }

        public static void AddDbLogging<T>(this ILoggerFactory factory, IServiceProvider provider, LogLevel minimum) where T : DbContext
        {
            factory.AddDbLogging<T>(provider, (name, level) => { return name != "Microsoft.EntityFrameworkCore.Infrastructure" && name != "Microsoft.EntityFrameworkCore.Database.Command" && level >= minimum; });
        }
    }
}