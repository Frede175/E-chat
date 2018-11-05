package Acquaintence;

import java.util.ArrayList;

public interface IUser {
    String getID();
    String getName();
    ArrayList<String> getRoles();
    ArrayList<String> getPermissions();
    void setName(String name);
}
