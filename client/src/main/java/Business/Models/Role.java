package Business.Models;

import Acquaintence.IRole;

public class Role implements IRole {

    private String id;
    private String name;

    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Role) {
            return id.equals(((Role) o).getId());
        }
        return false;
    }
}
