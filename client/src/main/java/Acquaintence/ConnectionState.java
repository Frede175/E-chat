package Acquaintence;

public enum ConnectionState {
    SUCCESS("Succes"),
    WRONG_LOGIN("Wrong login"),
    NO_BASIC_PERMISSIONS("No basic permissions"),
    NO_CONNECTION("No Connection available"),
    ERROR("Uknown error"),
    UNAUTHORIZED("Unauthorized"),
    SERVERERROR("Internal server error"),
    NOT_FOUND("Not found"),
    BAD_REQUEST("Bad request");

    String level;

    ConnectionState(String i) { level = i; }

    public String getLevel() {
        return level;
    }
}
