package Business.Models;

public class Login {
    private String type_token;
    private String access_token;
    private String error;
    private int expires_in;

    public String getType_token() {
        return type_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getError() {
        return error;
    }

    public String getAccess_token() {
        return access_token;
    }
}


