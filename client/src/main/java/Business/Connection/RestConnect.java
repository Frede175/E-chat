package Business.Connection;


import Acquaintence.ConnectionState;
import Business.Interfaces.IParameters;
import Business.Models.Login;
import com.google.gson.Gson;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestConnect {

    private final Gson gson = new Gson();

    private final HttpClient client;
    private final HttpRequestBase request;
    private final PathEnum path;

    RestConnect(PathEnum path, String token, String host, Object content, Object route, IParameters parameters, HttpClient client, HttpRequestBase request) throws URISyntaxException {
        this.client = client;
        this.request = request;
        this.path = path;


        URIBuilder uriBuilder = new URIBuilder(host);
        if (route != null) uriBuilder.setPath(path.getPath() + route.toString());
        else uriBuilder.setPath(path.getPath());

        if (parameters != null) uriBuilder.setParameters(parameters.getParameters());

        this.request.setURI(uriBuilder.build());

        if (content != null) {
            if (!(this.request instanceof HttpEntityEnclosingRequest)) throw new IllegalArgumentException("Request is not of type HttpEntityEnclosingRequest");

            this.request.setHeader(HttpHeaders.CONTENT_TYPE, path.getContentType().getMimeType());
            this.request.setHeader(HttpHeaders.CONTENT_ENCODING, Consts.UTF_8.name());

            if (path.getContentType() == ContentType.APPLICATION_JSON) {
                ((HttpEntityEnclosingRequest) this.request).setEntity(new StringEntity(gson.toJson(content), Consts.UTF_8));
            } else if (path.getContentType() == ContentType.APPLICATION_FORM_URLENCODED) {
                if (content instanceof IParameters) {
                    ((HttpEntityEnclosingRequest) this.request).setEntity(new UrlEncodedFormEntity(((IParameters) content).getParameters(), Consts.UTF_8));
                } else {
                    throw new IllegalArgumentException("Content is not of type IParameters");
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (path.getContentType() != null) {
            request.setHeader(HttpHeaders.CONTENT_TYPE, path.getContentType().getMimeType());
            request.setHeader(HttpHeaders.CONTENT_ENCODING, Consts.UTF_8.name());
        }

        if (token != null) {
            this.request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }

    }

    public <TContent> RequestResponse<TContent> execute() {
        try {
            HttpResponse response = client.execute(request);
            return getResponse(response);
        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }
    }

    private <TContent> RequestResponse<TContent> getResponse(HttpResponse response) {
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                System.out.println("Ok, Object returned");
                return getResult(response);

            case 201:
                System.out.println("Object created and return");
                return getResult(response);

            case 204:
                System.out.println("Ok, nothing returned");
                return new RequestResponse<>(null,ConnectionState.SUCCESS);

            case 400:
                System.out.println("Error, bad request");
                return new RequestResponse<>(null,ConnectionState.BAD_REQUEST);

            case 401:
                System.out.println("Unautherized");
                return new RequestResponse<>(null,ConnectionState.UNAUTHERIZED);

            case 404:
                System.out.println("Not found");
                return new RequestResponse<>(null,ConnectionState.NOT_FOUND);

            case 500:
                System.out.println("Internal Sever Error");
                return new RequestResponse<>(null,ConnectionState.SERVERERROR);
            default:
                return new RequestResponse<>(null, ConnectionState.ERROR);

        }
    }

    private <TResult> RequestResponse<TResult> getResult(HttpResponse response) {
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Type type = path.getResultType();
            System.out.println(result.toString());
            TResult obj = gson.fromJson(result.toString(), type);
            return new RequestResponse<>(obj, ConnectionState.SUCCESS);
        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

    }


}
