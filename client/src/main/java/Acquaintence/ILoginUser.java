package Acquaintence;

import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoginUser {

    String getSub();

    String getName();

    ArrayList<String> getRoles();

    ArrayList<PermissionEnum> getUserPermissions();

    ArrayList<PermissionEnum> getUserPermissionsFromType(PermissionType type);

    ArrayList<PermissionEnum> getAdminPermissions();

    void setName(String name);

}
