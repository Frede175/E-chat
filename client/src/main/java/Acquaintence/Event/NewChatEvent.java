package Acquaintence.Event;

import Acquaintence.IChat;

import java.util.EventObject;

public class NewChatEvent extends EventObject {
    private final IChat chat;

    public NewChatEvent(Object source, IChat chat) {
        super(source);
        this.chat = chat;
    }

    public IChat getChat() {
        return chat;
    }
}
