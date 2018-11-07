package Acquaintence.Event;

import Acquaintence.IMessageIn;

import java.util.EventObject;

public class MessageEvent extends EventObject{

    private final IMessageIn messageIn;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MessageEvent(Object source, IMessageIn messageIn) {
        super(source);
        this.messageIn = messageIn;
    }

    public IMessageIn getMessageIn() {
        return messageIn;
    }
}
