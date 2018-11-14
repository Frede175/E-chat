package Business.Connection;

public enum PermissionType {
    DEPARTMENT("Department"),
    USER("User"),
    CHAT("Chat"),
    ROLE("Role");

    private String name;

    private PermissionType(String name) {
        this.name = name;
    }
}
