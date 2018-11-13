package Acquaintence;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoginUser {

    public String getSub();

    public String getName();

    public ArrayList<String> getRoles();

    public ArrayList<String> getPermissions();

    public void setName(String name);

    public HashMap<String, String> toMap();
}
