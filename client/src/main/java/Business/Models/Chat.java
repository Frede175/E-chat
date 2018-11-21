package Business.Models;

import Acquaintence.IChat;
import Acquaintence.IMessageIn;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Chat implements IChat {

    private int id;
    private String name;
    private Set<MessageIn> messages;
    private boolean isGroupChat;

    public Chat() {
        messages = new TreeSet<>();
    }

    public Chat(String name) {
        this.name = name;
        messages = new TreeSet<>();
    }


    public Chat(int id, String name, boolean isGroupChat) {
        this.id = id;
        this.name = name;
        messages = new TreeSet<>();
        this.isGroupChat = isGroupChat;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<? extends IMessageIn> getMessages() {
        return messages;
    }

    @Override
    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void addMessage(MessageIn message) {
        messages.add(message);
    }


    public void addMessages(List<MessageIn> messages) {
        this.messages.addAll(messages);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
