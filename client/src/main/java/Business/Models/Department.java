package Business.Models;

import Acquaintence.IDepartment;

public class Department implements IDepartment{

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
}
