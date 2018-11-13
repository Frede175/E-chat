package Business.Connection;


import Acquaintence.ConnectionState;
import Acquaintence.IToMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.impl.Trie2;
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
import java.util.*;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestConnect {

    private String url = "https://localhost:5001";
    private final String formType = "application/x-www-form-urlencoded";
    //private final String url = "https://ptsv2.com";
    private final Gson gson;
    // /api/values

    public RestConnect() {
        gson = new Gson();
    }

    public RequestResponse<String> login(String username, String password) {

        try {

            HttpClient client = HttpClientBuilder.create().build();

            HttpPost request = new HttpPost(url + "/connect/token/");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("grant_type", "password"));
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));
            request.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = client.execute(request);



            System.out.println("Response Code Login: "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }


            JsonObject json = new JsonParser().parse(result.toString()).getAsJsonObject();

            if (json != null) {
                if(json.get("error") != null) {
                    return new RequestResponse<>(null, ConnectionState.WRONG_LOGIN);
                }
                return new RequestResponse<>(json.get("access_token").getAsString(), ConnectionState.SUCCESS);
            }
            return null;

        } catch (IOException e) {
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }
    }

    public <T, T1> RequestResponse<T> get(PathEnum path, T1 route, HashMap<String, String> param, String token) {
        {
            try {

                HttpClient client = HttpClientBuilder.create().build();

                HttpGet request = request(path, route, param, token);

                HttpResponse response = null;



                response = client.execute(request);

                System.out.println("Response Code Get : "
                        + response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }


                Type type = path.getResultType();

                System.out.println(result.toString());


                T obj = gson.fromJson(result.toString(), type);

                return new RequestResponse<T>(obj, ConnectionState.SUCCESS);

            } catch (IOException e) {
                e.printStackTrace();
                return new RequestResponse<T>(null, ConnectionState.NO_CONNECTION);
            }

        }
    }
    public <T, T1, T2> RequestResponse<T2> post(PathEnum path, T route, T1 content, String token) {
        try {

            HttpClient client = HttpClientBuilder.create().build();

            HttpPost request = request(path, route, null, token);
            StringEntity postingString = new StringEntity(gson.toJson(content));
            request.setEntity(postingString);

            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
            System.out.println("Response Code Post: "
                    + response.getStatusLine().getStatusCode());
            return new RequestResponse<>(null, ConnectionState.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }
    }


    public <T, T1> RequestResponse<T1> delete(PathEnum path, T route, String token) {
        try {

            HttpClient client = HttpClientBuilder.create().build();

            HttpDelete request = request(path, route, null, token);


            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
            System.out.println("Response Code Post: "
                    + response.getStatusLine().getStatusCode());
            return new RequestResponse<>(null, ConnectionState.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }
    }

    //TODO implement
    public <T , T1, T2> RequestResponse<T2> put(PathEnum path, T route, T1 content, String token) {
        try {
            HttpClient client = HttpClientBuilder.create().build();

            HttpPut request = request(path, route, null, token);
            StringEntity postingString = new StringEntity(gson.toJson(content));


            request.setEntity(postingString);

            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
            System.out.println("Response Code Post: "
                    + response.getStatusLine().getStatusCode());
            return new RequestResponse<>(null, ConnectionState.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new RequestResponse<>(null, ConnectionState.NO_CONNECTION);
        }
    }

    /***
     *
     * @param path Which call from client
     * @param route The route variable
     * @param param Params for get method (After the ?)
     * @param token The login token
     * @param <T> Generic type of the route parameter
     * @param <T1> The respective request type
     * @return The respective request
     */
    public <T, T1> T1 request(PathEnum path, T route, HashMap<String, String> param, String token) {
        HttpRequestBase request = null;
        String url = this.url + path.getPath();
        if (route != null) {
            url = url.concat(route.toString());
        }
        if(param != null) {
            url = url.concat("?");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                url = url.concat(entry.getKey() + "=" + entry.getValue() + "&");
            }//TODO Better fix for &
            url = url.substring(0, url.length() - 1);
        }
        System.out.println("URL: " + url);
        //TODO delete this line
        //url = url.concat("?departmenId=1");


        switch (path.getType()){
            case POST: {

                request = new HttpPost(url);
                break;
            }
            case GET: {
                request = new HttpGet(url);
                break;
            }

            case DELETE: {
                request = new HttpDelete(url);
                break;
            }

            case PUT: {
                request = new HttpPut(url);
                break;
            }


        }
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Authorization", "Bearer " + token);


        return (T1) request;
    }



}
