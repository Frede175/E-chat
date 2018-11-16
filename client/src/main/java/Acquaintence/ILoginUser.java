package Acquaintence;

import Business.Connection.PermissionEnum;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoginUser {

    public String getSub();

    public String getName();

    public ArrayList<String> getRoles();

    public ArrayList<PermissionEnum> getUserPermissions();

    public ArrayList<PermissionEnum> getAdminPermissions();

    public void setName(String name);

    public HashMap<String, String> toMap();
}
