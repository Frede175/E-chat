package Business.Connection;

import Acquaintence.ConnectionState;
import Acquaintence.IRequestResponse;

public class RequestResponse<T> implements IRequestResponse {

    private T response;
    private ConnectionState connectionState;

    public RequestResponse(T response, ConnectionState connectionState) {
        this.response = response;
        this.connectionState = connectionState;
    }

    public RequestResponse() {
    }

    @Override
    public T getResponse() {
        return response;
    }

    @Override
    public ConnectionState getConnectionState() {
        return connectionState;
    }
}
