package Acquaintence.Event;


import Acquaintence.IChat;

import java.util.EventObject;


public class ChangeChatListEvent extends EventObject {

    private final IChat chat;

    public ChangeChatListEvent(Object source, IChat chat) {
        super(source);
        this.chat = chat;
    }

    public IChat getChat() {
        return chat;
    }
}
