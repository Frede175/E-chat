package Business.Models;

import Acquaintence.IDepartment;
import Acquaintence.IToMap;

import java.util.HashMap;
import java.util.Map;

public class Department implements IDepartment, IToMap {

    private int ID;
    private String name;

    public Department(int ID, String name) {
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
        map.put("departmenId", String.valueOf(ID));
        return map;
    }
}
