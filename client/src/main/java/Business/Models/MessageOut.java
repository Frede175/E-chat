package Business.Models;

import Acquaintence.IMessageOut;

public class MessageOut implements IMessageOut {

    private String content;
    private int chatId;

    public MessageOut (String content, int chatId) {
        this.content = content;
        this.chatId = chatId;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public int getChatId() {
        return this.chatId;
    }
}
