package Acquaintence.Event;

import java.util.EventObject;

public class DeleteChatEvent extends EventObject {

    private final int chat;

    public int getChatId() {
        return chat;
    }

    public DeleteChatEvent(Object source, int chat) {
        super(source);
        this.chat = chat;
    }
}
