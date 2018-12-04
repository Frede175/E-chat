package Business.Connection;

public enum PermissionType {
    DEPARTMENT("Department"),
    USER("User"),
    CHAT("Chat"),
    ROLE("Role"),
    LOG("Log");

    private String name;

    private PermissionType(String name) {
        this.name = name;
    }
}
