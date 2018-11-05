package Business.Models;

import Acquaintence.IToMap;
import Acquaintence.IUser;

import java.util.ArrayList;
import java.util.HashMap;

public class User implements IUser, IToMap {

    private String ID;
    private String name;
    private ArrayList<String> roles;
    private ArrayList<String> permissions;

    public User(String ID, String name, ArrayList<String> roles, ArrayList<String> permissions) {
        this.ID = ID;
        this.name = name;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<String> getRoles() {
        return roles;
    }

    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", ID);
        return map;
    }
}
