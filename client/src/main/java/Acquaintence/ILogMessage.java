package Acquaintence;

import java.util.Date;

public interface ILogMessage extends Comparable<ILogMessage>{
    int getId();
    LogLevel getLogLevelEnum();
    String getMessage();
    Date getTimeStamp();
    int getLogLevel();
}
