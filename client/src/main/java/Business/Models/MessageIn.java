package Business.Models;

import Acquaintence.IMessageIn;

import java.util.Date;

public class MessageIn implements IMessageIn {

    private int Id;
    private int chatId;
    private String content;
    private Date timeStamp;
    private User user;

    public MessageIn() {

    }


    public MessageIn(int Id, String content, Date timeStamp, User user, int chatId) {
        this.Id = Id;
        this.content = content;
        this.timeStamp = timeStamp;
        this.user = user;
        this.chatId = chatId;
    }

    @Override
    public int getID() {
        return Id;
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

    @Override
    public int getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageIn) {
            return timeStamp.equals(((MessageIn) o).timeStamp);
        }
        return false;
    }

    @Override
    public int compareTo(IMessageIn o) {
        return timeStamp.compareTo(o.getTimeStamp());
    }
}
