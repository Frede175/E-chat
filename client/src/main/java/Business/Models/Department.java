package Business.Models;

import Acquaintence.IDepartment;
import Business.Interfaces.IParameters;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Department implements IDepartment, IParameters {

    private int id;
    private String name;

    public Department(int Id, String name) {
        this.id = Id;
        this.name = name;
    }

    public Department(String name){
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("departmentId", String.valueOf(id));
        return map;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("departmentId", String.valueOf(id)));
        return nvps;
    }
}
