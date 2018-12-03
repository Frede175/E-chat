namespace Server.Logging
{
    public interface IDbLoggingHandler
    {
        void Add(LogMessage message);
    }
}