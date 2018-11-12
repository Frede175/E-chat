package Acquaintence;

import java.util.ArrayList;

public interface IUser {
    String getSub();
    String getName();
    ArrayList<String> getRoles();
    ArrayList<String> getPermissions();
    void setName(String name);
}
