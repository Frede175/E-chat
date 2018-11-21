package Acquaintence;

import java.util.List;
import java.util.Set;

public interface IChat {
    int getId();
    String getName();
    void setName(String name);
    Set<? extends IMessageIn> getMessages();
    boolean isGroupChat();
    String toString();
}
