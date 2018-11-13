package Acquaintence;

import java.util.Date;

public interface IMessageIn extends Comparable<IMessageIn> {
    int getID();
    String getContent();
    Date getTimeStamp();
    IUser getUser();
    int getChatId();
}
