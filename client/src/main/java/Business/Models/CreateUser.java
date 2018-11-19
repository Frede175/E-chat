package Business.Models;

import Acquaintence.ICreateUser;
import Acquaintence.IToMap;

import java.util.HashMap;

public class CreateUser implements ICreateUser, IToMap {
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

    @Override
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("UserName", String.valueOf(userName));
        map.put("Password", String.valueOf(password));
        return map;
    }
}
