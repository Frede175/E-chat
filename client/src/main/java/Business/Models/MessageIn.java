package Business.Models;

import Acquaintence.IMessageIn;

import java.util.Date;

public class MessageIn implements IMessageIn {

    private int ID;
    private int chatID;
    private String content;
    private Date timeStamp;
    private User user;


    public MessageIn(int ID, String content, Date timeStamp, User user, int chatID) {
        this.ID = ID;
        this.content = content;
        this.timeStamp = timeStamp;
        this.user = user;
        this.chatID = chatID;
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

    public int getChatID() {
        return chatID;
    }

    @Override
    public int compareTo(IMessageIn o) {
        return timeStamp.compareTo(o.getTimeStamp());
    }
}
