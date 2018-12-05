package Acquaintence;

import java.util.Date;

public interface ILogMessage extends Comparable<ILogMessage>{
    int getId();
    LogLevel getLogLevel();
    String getMessage();
    Date getTimeStamp();
}
