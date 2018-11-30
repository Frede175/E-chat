using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Timers;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

namespace Server.Logging
{
    public class DbLoggingHandler<T> : IDbLoggingHandler where T : DbContext
    {
        private const int BATCH_SIZE = 20;

        private const int TIMER_INTERVAL = 5 * 1000;
        private readonly Queue<LogMessage> _messages;

        System.Timers.Timer timer;

        private readonly IServiceProvider _serviceProvider;
        public DbLoggingHandler(IServiceProvider serviceProvider)
        {
            timer = new System.Timers.Timer(TIMER_INTERVAL);

            timer.Elapsed += Elapsed;
            timer.AutoReset = false;
            timer.Enabled = true;

            _messages = new Queue<LogMessage>();
            _serviceProvider = serviceProvider;
        }

        public void Add(LogMessage message)
        {
            bool shouldRun = false;
            lock (_messages)
            {
                _messages.Enqueue(message);
                if (_messages.Count >= BATCH_SIZE) 
                {
                    shouldRun = true;
                }
            }
            RestartTimer();
            if (shouldRun) Task.Run(() => ProcessAsync());

        }

        private void RestartTimer() 
        {
            timer.Stop();
            timer.Start();
        }

        private void Elapsed(Object source, ElapsedEventArgs e)
        {
            Task.Run(() => ProcessAsync());
        }



        private async Task ProcessAsync()
        {
            LogMessage[] currentMessages = new LogMessage[BATCH_SIZE];
            int count;
            lock (_messages)
            {
                if (!_messages.Any())
                {
                    Monitor.Exit(_messages);
                    return;
                }

                for (count = 0; count < BATCH_SIZE && _messages.Any(); count++) 
                {
                    currentMessages[count] = _messages.Dequeue();
                }
                
            }

            using (var scope = _serviceProvider.CreateScope())
            {
                var context = scope.ServiceProvider.GetRequiredService<T>();
                context.Set<LogMessage>().AddRange(currentMessages.Take(count));
                await context.SaveChangesAsync();
            }

        }
    }
}