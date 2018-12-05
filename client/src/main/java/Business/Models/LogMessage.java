package Business.Models;

import Acquaintence.ILogMessage;
import Acquaintence.IMessageIn;
import Acquaintence.LogLevel;


import java.util.Date;

public class LogMessage implements ILogMessage {

    private int id;
    private LogLevel logLevel;
    private int logLevelInt;
    private String message;
    private Date timeStamp;

    public LogMessage() {

    }

    public LogMessage(int id, int logLevel, String message, Date timeStamp) {
        this.id = id;
        this.logLevelInt = logLevel;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public void initializeLogLevel() {
        logLevel = LogLevel.values()[logLevelInt];
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LogLevel getLogLevel() {
        return logLevel;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "" + timeStamp;
    }

    @Override
    public int compareTo(ILogMessage o) {
        return timeStamp.compareTo(o.getTimeStamp());
    }
}