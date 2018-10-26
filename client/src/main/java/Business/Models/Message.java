package Business.Models;

import Acquaintence.IMessage;

import java.util.Date;

public class Message implements IMessage {

    private int ID;
    private String content;
    private Date timeStamp;
    private User user;


    public Message(int ID, String content, Date timeStamp, User user) {
        this.ID = ID;
        this.content = content;
        this.timeStamp = timeStamp;
        this.user = user;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public User getUser() {
        return user;
    }
}
