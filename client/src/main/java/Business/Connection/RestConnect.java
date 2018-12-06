package Business.Connection;


import Acquaintence.ConnectionState;
import Business.Models.Login;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
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

    private String url = "http://localhost:5000";
    private final String formType = "application/x-www-form-urlencoded";
    //private final String url = "https://ptsv2.com";
    private final Gson gson = new Gson();
    private HttpClient client;
    private HttpRequestBase request;
    private PathEnum path;
    private String token;
    // /api/values

    public RestConnect() {
    }

    public RestConnect(PathEnum path) {
        this.client = HttpClientBuilder.create().build();
        this.path = path;
    }

    public RestConnect(PathEnum path, String token) {
        this.client = HttpClientBuilder.create().build();
        this.path = path;
        this.token = token;
    }

    public RestConnect(HttpClient client, PathEnum path, String token) {
        this.client = client;
        this.path = path;
        this.token = token;
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

    public RestConnect create() {
        switch (path.getType()) {
            case POST:
            case LOGIN:
            case LOGOUT:
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
        return this;

    }

    public RestConnect create(HttpRequestBase request) {
        switch (path.getType()) {
            case POST:
                if (!(request instanceof HttpPost)) {
                    throw new IllegalArgumentException();
                }
                break;
            case GET:
                if (!(request instanceof HttpGet)) {
                    throw new IllegalArgumentException();
                }
                break;
            case PUT:
                if (!(request instanceof HttpPut)) {
                    throw new IllegalArgumentException();
                }
                break;
            case DELETE:
                if (!(request instanceof HttpDelete)) {
                    throw new IllegalArgumentException();
                }
                break;
        }

        this.request = request;
        return this;
    }

    public <TResult> RequestResponse executeNoParameters() {
        return execute(null,null);
    }

    public <TResult, TRoute> RequestResponse<TResult> executeRoute(TRoute route) {
        return execute(route, null);
    }

    public <TResult, TContent> RequestResponse<TResult> executeContent(TContent content) {
        return execute(null, content);
    }

    public <TResult, TRoute, TContent> RequestResponse<TResult> execute(TRoute route, TContent content) {
        String url = makeUrl(route, content);

        try {
            request.setURI(new URI(url));
        } catch (URISyntaxException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

        addHeaders();

        if (path.getType() == ConnectionType.POST || path.getType() == ConnectionType.PUT)
        try {
            setEntity(content);
        } catch (UnsupportedEncodingException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }

        return getResponse(response);

    }

    public RequestResponse<Login> login(String password, String username) {
        String url = makeUrl(null,null);

        try {
            request.setURI(new URI(url));
        } catch (URISyntaxException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

        try {
            setLoginHeader(password,username);
        } catch (UnsupportedEncodingException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }

        return getResult(response);
    }

    public RequestResponse<String> logout() {
        String url = makeUrl(null,null);

        try {
            request.setURI(new URI(url));
        } catch (URISyntaxException e) {
            return new RequestResponse<>(null, ConnectionState.ERROR);
        }

        addHeaders();

        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }

        if(response.getStatusLine().getStatusCode() == 200) {
            return new RequestResponse<>("You have been logged out", ConnectionState.SUCCESS);
        } else
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
    }

    private <TResult> RequestResponse<TResult> getResult(HttpResponse response) {
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
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

    private void addHeaders() {

        switch (path.getType()) {
            case LOGIN:
                break;
            case LOGOUT:
                request.addHeader("Authorization", "Bearer " + token);
                request.addHeader("Content-type", "application/x-www-form-urlencoded");
                break;
            case POST:
            case PUT:
                request.addHeader("Authorization", "Bearer " + token);
                request.addHeader("User-Agent", USER_AGENT);
                request.addHeader("Content-type", "application/json; charset=UTF-8");
                break;
            case DELETE:
            case GET:
                request.addHeader("Authorization", "Bearer " + token);
                request.addHeader("User-Agent", USER_AGENT);
                break;
        }

    }

    private <TContent> void setEntity(TContent content) throws UnsupportedEncodingException {
        ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(gson.toJson(content)));
    }

    private <TRoute, TContent> String makeUrl(TRoute route, TContent content) {
        String url = this.url + path.getPath();
        if (route != null) {
            url = url.concat(route.toString());
        }

        if (content instanceof HashMap && path.getType() == ConnectionType.GET) {
            HashMap<String,String> newContent = (HashMap<String,String>) content;
            url = url.concat("?");
            for (Map.Entry<String, String> entry : newContent.entrySet()) {
                url = url.concat(entry.getKey() + "=" + entry.getValue() + "&");
            }//TODO Better fix for &
            url = url.substring(0, url.length() - 1);
        }
        System.out.println(url);
        return url;
    }

    private void setLoginHeader(String password, String username) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("grant_type", "password"));
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        ((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
    }



}
