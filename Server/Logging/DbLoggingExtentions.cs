using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Server.Logging
{
    public static class DbLoggingExtentions
    {
        public static ILoggingBuilder AddDbLogging<T>(this ILoggingBuilder builder, ServiceProvider serviceProvider) where T : DbContext
        {
            return builder.AddProvider(new DbLoggingProvider<T>(serviceProvider));
        }
    }
}