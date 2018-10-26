package Acquaintence;

import java.util.Date;

public interface IMessageIn {
    int getID();
    String getContent();
    Date getTimeStamp();
    IUser getUser();
}
