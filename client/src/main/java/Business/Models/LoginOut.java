package Business.Models;

import Business.Interfaces.IParameters;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginOut implements IParameters {

    private final String password;
    private final String username;
    private final String grantType = "password";

    public LoginOut(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public List<NameValuePair> getParameters() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("password", password));
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("grant_type", grantType));
        return nvps;
    }
}
