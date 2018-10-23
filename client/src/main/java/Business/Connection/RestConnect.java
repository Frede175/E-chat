package Business.Connection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestConnect {


    private void get(String token) {
        try {
            String url = "https://localhost:5001/api/values";

            HttpClient client = HttpClientBuilder.create().build();

            HttpGet request = new HttpGet(url);

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

}
