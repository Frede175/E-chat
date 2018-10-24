package client;

import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.CreateUser;
import Business.Models.Department;
import Business.Models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private final String jsonType = "application/json";
    private final String formType = "application/x-www-form-urlencoded";
    private final String username = "admin";
    private final String password = "AdminAdmin123*";

    private HubConnection chatConnection;

    private IMessageReceiver messageReceiver;

    public Client(IMessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;

        RestConnect rest = new RestConnect();
        String result = rest.post(PathEnum.CreateUser, null, new CreateUser(username, password), null);
        System.out.println(result);

        String token = rest.login(username, password);
        System.out.println("Token: " + token);


        //rest.post(PathEnum.CreateDepartment, null, new Department(0, "Fred"), token);
        List<Department> d = rest.get(PathEnum.GetDepartments, token);

        for (Department department : d) {
            System.out.println(department.getName());
        }


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
}



