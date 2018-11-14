package Business.Models;

import Acquaintence.ILoginUser;
import Acquaintence.IToMap;
import Business.Connection.PermissionEnum;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginUser implements ILoginUser, IToMap {

    private String sub;
    private String name;
    private ArrayList<String> roles;
    private ArrayList<String> userPermissions;
    private ArrayList<String> adminPermissions;

    public LoginUser(String sub, String name, ArrayList<String> roles, ArrayList<String> userPermissions, ArrayList<String> adminPermissions) {
        this.sub = sub;
        this.name = name;
        this.roles = roles;
        this.userPermissions = userPermissions;
        this.adminPermissions = userPermissions;
    }

    @Override
    public String getSub() {
        return sub;
    }

    /*
    public void createList(ArrayList<String> permissions) {
        private ArrayList<PermissionEnum>
        for(String permission : permissions) {

        }
    }
    */

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<String> getAdminPermissions() {
        return adminPermissions;
    }

    @Override
    public ArrayList<String> getRoles() {
        return roles;
    }

    @Override
    public ArrayList<String> getUserPermissions() {
        return userPermissions;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", sub);
        return map;
    }
}
