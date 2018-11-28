using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace Server.Logging
{
    public class DbLoggingHandler<T> : IDbLoggingHandler where T : DbContext
    {
        private readonly Queue<LogMessage> _messages;

        //private readonly DbContext _logginContext;

        public DbLoggingHandler(T context) //DbContext logginContext
        {
            //_logginContext = logginContext;
            _messages = new Queue<LogMessage>();
            Task.Run(() => Dequeue());
        }

        public void AddToQueue(LogMessage message)
        {
            lock (_messages)
            {
                _messages.Enqueue(message);
                Monitor.Pulse(_messages);
            }
        }

        private void Dequeue()
        {
            LogMessage message;

            while (true)
            {
                lock (_messages)
                {
                    if (!_messages.Any())
                    {
                        Monitor.Wait(_messages);
                        Monitor.Enter(_messages);
                    }
                    message = _messages.Dequeue();
                }
                Process(message);
            }
        }

        private void Process(LogMessage message)
        {
            //TODO db.
            Debug.Write(message);
        }
    }
}