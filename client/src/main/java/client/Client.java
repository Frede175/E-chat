package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.LogLevel;
import io.reactivex.Single;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class Client {

    private final String jsonType = "application/json";
    private final String formType = "application/x-www-form-urlencoded";

    private HubConnection chatConnection;

    private IMessageReceiver messageReceiver;

    public Client(IMessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
        CreateUser();
        String token = login();
        get(token);

        chatConnection = HubConnectionBuilder.create("https://localhost:5001/hubs/chat")
                .withAccessTokenProvider(Single.just(token))
                .build();

        //Add message receive method callback
        chatConnection.on("ReceiveMessage", this.messageReceiver::receive, MessageObject.class);

        //Start the connection
        chatConnection.start().blockingAwait();

    }

    public void close() {
        chatConnection.stop();
    }

    public void send(String message) {
        String s = "";
        try {
            chatConnection.send("SendMessage", new MessageObject("Fred", s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreateUser() {
        String result = sendPost("/api/Auth", "{ \"username\":\"admin\",\"password\": \"AdminAdmin123*\" }", jsonType);
        System.out.println(result);
    }

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


    private String login() {




        //request.addHeader("User-Agent", USER_AGENT);

        String result = sendPost("/connect/token", "grant_type=password&username=admin&password=AdminAdmin123*", formType);
        System.out.printf(result);
        JsonObject json = new JsonParser().parse(result).getAsJsonObject();

        if (json != null) {
            return json.get("access_token").getAsString();
        }
        return null;
    }

    private String sendPost(String path, String parameters, String contentType) {
        String url = "https://localhost:5001" + path;
        byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection con = null;

        String out = null;


        try {
            URL myUrl = new URL(url);

            con = (HttpURLConnection) myUrl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", contentType);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
                out = content.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert con != null;
            con.disconnect();
        }

        return out;
    }


}
