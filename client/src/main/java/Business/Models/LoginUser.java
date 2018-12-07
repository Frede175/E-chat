package Business.Models;

import Acquaintence.ILoginUser;
import Business.Interfaces.IParameters;
import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class LoginUser implements ILoginUser, IParameters {

    private String sub;
    private String name;
    private ArrayList<String> roles;
    private Set<PermissionEnum> userPermissions;
    private Set<PermissionEnum> adminPermissions;
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
        userPermissions = new HashSet<>();
        adminPermissions = new HashSet<>();
        for(String permission : this.permissions) {
            PermissionEnum permissionEnum = PermissionEnum.valueOf(permission);
            if(permissionEnum.hasElevatedPermission()) {
                this.adminPermissions.add(permissionEnum);
            } else {
                this.userPermissions.add(permissionEnum);
            }
        }
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<PermissionEnum> getAdminPermissions() {
        return adminPermissions;
    }

    @Override
    public Set<PermissionEnum> getUserPermissions() {
        return userPermissions;
    }

    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public ArrayList<PermissionEnum> getUserPermissionsFromType(PermissionType type) {
        ArrayList<PermissionEnum> permissions = new ArrayList();
        for (PermissionEnum perm : adminPermissions) {
            if(perm.getType() == type) {
                permissions.add(perm);
            }
        }
        return permissions;
    }

    @Override
    public ArrayList<String> getRoles() {
        return roles;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", sub);
        return map;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("userId", sub));
        return nvps;
    }
}
