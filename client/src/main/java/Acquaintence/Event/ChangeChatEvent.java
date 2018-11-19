package Acquaintence.Event;

import Acquaintence.IChat;

import java.util.EventObject;

public class ChangeChatEvent extends EventObject{

    private final IChat chat;

    public ChangeChatEvent(Object source, IChat chat) {
        super(source);
        this.chat = chat;
    }

    public IChat getChat() {
        return chat;
    }
}
