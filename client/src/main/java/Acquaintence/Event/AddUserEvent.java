package Acquaintence.Event;

import Acquaintence.IUser;

import java.util.EventObject;

public class AddUserEvent extends EventObject {
    private final IUser user;

    public AddUserEvent(Object source, IUser user) {
        super(source);
        this.user = user;
    }

    public IUser getUser() {
        return user;
    }
}
