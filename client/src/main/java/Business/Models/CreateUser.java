package Business.Models;

import Acquaintence.ICreateUser;

public class CreateUser implements ICreateUser {
    private String username;
    private String password;

    public CreateUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
