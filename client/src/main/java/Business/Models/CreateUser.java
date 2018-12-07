package Business.Models;

import Acquaintence.ICreateUser;


import java.util.ArrayList;

public class CreateUser implements ICreateUser {
    private String userName;
    private String password;
    private String role;
    private ArrayList<Integer> departmentIds;

    public CreateUser(String username, String password, String role, ArrayList<Integer> departmentIds) {
        this.userName = username;
        this.password = password;
        this.role = role;
        this.departmentIds = departmentIds;
    }


    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }



    @Override
    public String getRole() {
        return role;
    }

    @Override
    public ArrayList<Integer> getDepartmentIds() {
        return departmentIds;
    }
}
