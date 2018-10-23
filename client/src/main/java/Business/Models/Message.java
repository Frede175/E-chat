package Business.Models;

import Acquaintence.IMessage;

import java.util.Date;

public class Message implements IMessage {

    private int ID;
    private String content;
    private Date timeStamp;
    private String senderID;


    public Message(int ID, String content, Date timeStamp, String senderID) {
        this.ID = ID;
        this.content = content;
        this.timeStamp = timeStamp;
        this.senderID = senderID;
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
    public String getSenderID() {
        return senderID;
    }
}
