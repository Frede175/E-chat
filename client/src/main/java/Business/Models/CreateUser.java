package Business.Models;

import Acquaintence.ICreateUser;

public class CreateUser implements ICreateUser {
    private String userName;
    private String password;

    public CreateUser(String username, String password) {
        this.userName = username;
        this.password = password;
    }


    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
