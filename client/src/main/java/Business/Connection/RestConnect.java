package Business.Connection;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestConnect {

    private final String url = "https://localhost:5001";
    private final String formType = "application/x-www-form-urlencoded";
    //private final String url = "https://ptsv2.com";
    private final Gson gson = new Gson();
    // /api/values

    public String login(String username, String password) {

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

            System.out.println(result.toString());

            JsonObject json = new JsonParser().parse(result.toString()).getAsJsonObject();

            if (json != null) {
                return json.get("access_token").getAsString();
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public <T> T get(PathEnum path, String token) {
        {
            try {


                HttpClient client = HttpClientBuilder.create().build();

                HttpGet request = new HttpGet(url + path.getPath());

                // add request header
                request.addHeader("User-Agent", USER_AGENT);
                request.addHeader("Authorization", "Bearer " + token);

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

                System.out.println(result.toString());

                Type type = path.getResultType();

                T newObject = gson.fromJson(result.toString(), type);

                return newObject;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    public <T, T1, T2> T2 post(PathEnum path, T param, T1 content, String token) {
        try {
            String url = this.url + path.getPath();
            if (param != null) {
                url = url.concat(param.toString());
                System.out.println(path);
            }

            HttpClient client = HttpClientBuilder.create().build();

            HttpPost request = new HttpPost(url);
            StringEntity postingString = new StringEntity(gson.toJson(content));
            request.setEntity(postingString);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = client.execute(request);
            System.out.println("Response Code Post: "
                    + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
