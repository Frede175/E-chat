package Acquaintence.Event;

import Acquaintence.IUser;

import java.util.EventObject;

public class LeaveChatEvent extends EventObject {
    private final int chatId;
    private final IUser user;

    public LeaveChatEvent(Object source, int chatId, IUser user) {
        super(source);
        this.chatId = chatId;
        this.user = user;
    }

    public int getChatId() {
        return chatId;
    }

    public IUser getUser() {
        return user;
    }
}
