package Business.Models;

import Acquaintence.IDepartment;
import Acquaintence.IToMap;

import java.util.HashMap;

public class Department implements IDepartment, IToMap {

    private int id;
    private String name;

    public Department(int Id, String name) {
        this.id = Id;
        this.name = name;
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
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("departmentId", String.valueOf(id));
        return map;
    }

    @Override
    public String toString() {
        return name;
    }
}
