package Business.Models;

import Acquaintence.IToMap;
import Acquaintence.IUser;

import java.util.HashMap;

public class User implements IUser, IToMap {

    private int ID;
    private String name;

    public User(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public int getID() {
        return ID;
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
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(ID));
        return map;
    }
}
