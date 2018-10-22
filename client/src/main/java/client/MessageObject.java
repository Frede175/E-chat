package client;

public class MessageObject {
    private String user;
    private String message;

    public MessageObject(String user, String message) {
        this.message = message;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
