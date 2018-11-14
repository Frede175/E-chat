package Business.Models;

import Acquaintence.ILoginUser;
import Acquaintence.IToMap;
import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginUser implements ILoginUser, IToMap {

    private String sub;
    private String name;
    private ArrayList<String> roles;
    private ArrayList<PermissionEnum> userPermissions;
    private ArrayList<PermissionEnum> adminPermissions;
    private ArrayList<String> permissions;

    public LoginUser(String sub, String name, ArrayList<String> roles, ArrayList<String> permissions) {
        this.sub = sub;
        this.name = name;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public String getSub() {
        return sub;
    }


    public void initializePermissions() {
        userPermissions = new ArrayList<>();
        adminPermissions = new ArrayList<>();
        for(String permission : this.permissions) {
            PermissionEnum permissionEnum = PermissionEnum.valueOf(permission);
            if(permissionEnum.hasElevatedPermission()) {
                System.out.println(permissionEnum.getPermission() + " added to AdminList");
                this.adminPermissions.add(permissionEnum);
            } else {
                System.out.println(permissionEnum.getPermission() + " added to UserList");
                this.userPermissions.add(permissionEnum);
            }
        }
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<PermissionEnum> getAdminPermissions() {
        return adminPermissions;
    }

    @Override
    public ArrayList<PermissionEnum> getUserPermissions() {
        return userPermissions;
    }

    @Override
    public ArrayList<String> getRoles() {
        return roles;
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
