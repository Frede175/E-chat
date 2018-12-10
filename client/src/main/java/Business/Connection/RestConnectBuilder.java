package Business.Connection;

import Business.Interfaces.IParameters;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URISyntaxException;

public class RestConnectBuilder {
    private PathEnum path;
    private String host;
    private String token;
    private HttpClient client;
    private HttpRequestBase request;
    private Object route;
    private Object content;
    private IParameters parameters;

    private RestConnectBuilder(final PathEnum path) {
        this.path = path;
    }

    public static RestConnectBuilder create(final PathEnum path) {
        return new RestConnectBuilder(path);
    }

    public RestConnectBuilder withHost(final String host) {
        this.host = host;
        return this;
    }

    public RestConnectBuilder withToken(final String token) {
        this.token = token;
        return this;
    }

    public RestConnectBuilder withClient(final HttpClient client) {
        this.client = client;
        return this;
    }

    public RestConnectBuilder withRequest(final HttpRequestBase request) {
        this.request = request;
        return this;
    }

    public RestConnectBuilder withRoute(final Object route) {
        this.route = route;
        return this;
    }

    public RestConnectBuilder withContent(final Object content) {
        this.content = content;
        return this;
    }

    public RestConnectBuilder withParameters(final IParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    private void createRequest() {
        switch (path.getType()) {
            case POST:
                request = new HttpPost();
                break;
            case GET:
                request = new HttpGet();
                break;
            case PUT:
                request = new HttpPut();
                break;
            case DELETE:
                request = new HttpDelete();
                break;
        }
    }

    public RestConnect build() {
        if (host == null) host = ConnectionConst.HOST;
        if (client == null) client = HttpClientBuilder.create().build();
        if (request == null) {
            createRequest();
        }

        try {
            return new RestConnect(path, token, host, content, route, parameters, client, request);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
