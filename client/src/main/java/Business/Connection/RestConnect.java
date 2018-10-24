package Business.Connection;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestConnect {

    //private final String url = "https://localhost:5001";
    private final String url = "https://localhost:5001";
    private final Gson gson = new Gson();
    // /api/values

    private void get(String path, String token) {
        try {

            HttpClient client = HttpClientBuilder.create().build();

            HttpGet request = new HttpGet(url + path);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Authorization", "Bearer " + token);

            HttpResponse response = null;

            response = client.execute(request);


            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public <T> void post(String path, T param, String token) {
        try {
            HttpClient client = HttpClientBuilder.create().build();

            HttpPost request = new HttpPost(url + path);
            StringEntity postingString = new StringEntity(gson.toJson(param));
            request.setEntity(postingString);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void put(String path, T param, String token) {
        try {
            HttpClient client = HttpClientBuilder.create().build();

            HttpPut request = new HttpPut(url + path);
            StringEntity postingString = new StringEntity(gson.toJson(param));
            request.setEntity(postingString);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
