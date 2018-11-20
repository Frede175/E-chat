package Business.Models;

import Acquaintence.ICreateUser;
import Acquaintence.IRole;

public class CreateUser implements ICreateUser {
    private String userName;
    private String password;
    private String role;

    public CreateUser(String username, String password, String role) {
        this.userName = username;
        this.password = password;
        this.role = role;
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
}
