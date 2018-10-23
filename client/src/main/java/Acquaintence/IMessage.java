package Acquaintence;

import java.util.Date;

public interface IMessage {
    int getID();
    String getContent();
    Date getTimeStamp();
    String getSenderID();
}
