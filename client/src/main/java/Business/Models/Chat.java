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

    public Chat() {
        messages = new TreeSet<>();
    }

    public Chat(String name) {
        this.name = name;
        messages = new TreeSet<>();
    }


    public Chat(int id, String name) {
        this.id = id;
        this.name = name;
        messages = new TreeSet<>();
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
