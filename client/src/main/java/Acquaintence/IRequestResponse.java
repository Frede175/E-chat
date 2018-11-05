package Acquaintence;

public interface IRequestResponse {
    <T> T getResponse();
    ConnectionState getConnectionState();
}
