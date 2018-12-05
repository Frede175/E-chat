package Acquaintence;

import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface ILoginUser {

    String getSub();

    String getName();

    ArrayList<String> getRoles();

    Set<PermissionEnum> getUserPermissions();

    ArrayList<PermissionEnum> getUserPermissionsFromType(PermissionType type);

    Set<PermissionEnum> getAdminPermissions();

    void setName(String name);

    public ArrayList<String> getPermissions();

}
