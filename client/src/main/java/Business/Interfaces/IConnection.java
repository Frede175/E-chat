package Business.Interfaces;

public interface IConnection {
    String login(String username, String password);

    void Connect();
}
