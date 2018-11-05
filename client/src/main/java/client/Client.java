package client;

import Acquaintence.IMessageReceiver;
import Business.Connection.PathEnum;
import Business.Connection.RestConnect;
import Business.Models.Chat;
import Business.Models.CreateUser;
import Business.Models.Department;
import Business.Models.MessageIn;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;

import java.util.List;

public class Client {

    private final String jsonType = "application/json";
    private final String formType = "application/x-www-form-urlencoded";
    private final String username = "admin";
    private final String password = "AdminAdmin123*";
    private Department department = new Department(1, "jeff");

    private HubConnection chatConnection;

    private IMessageReceiver messageReceiver;

    public Client(IMessageReceiver messageReceiver) {
        /*this.messageReceiver = messageReceiver;

        RestConnect rest = new RestConnect();
        String result = rest.post(PathEnum.CreateUser, null, new CreateUser(username, password), null);
        System.out.println("Jeff");
        //System.out.println(result);

        //String token = rest.login(username, password);
        System.out.println("We fucked");
        //System.out.println("Token: " + token);

        // rest.post(PathEnum.CreateChatroom, 2, new Chat(1, "Chat1"), token);

        //rest.delete(PathEnum.DeleteChatRoom, 1, token);

        //rest.put(PathEnum.PutChatRoom, 2, new Chat (1, "Chat1"), token);

        //rest.post(PathEnum.AddUserToChat, 2, "d8d65767-ca69-4abb-974e-a21883096b4e", token);

        //List<Chat> c = rest.get(PathEnum.GetChats, "d8d65767-ca69-4abb-974e-a21883096b4e", department.toMap(),  token);

        /* for (Chat chat : c) {
            System.out.println(chat.getName());
        } */


        /* rest.post(PathEnum.CreateDepartment, null, new Department(0, "VICTORY"), token);

        List<Department> d = rest.get(PathEnum.GetDepartments, null, department.toMap(), token);

        for (Department department : d) {
            System.out.println(department.getName());
        } */


        /*chatConnection = HubConnectionBuilder.create("https://localhost:5001/hubs/chat")
                .withAccessTokenProvider(Single.just(token))
                .build();

        //Add message receive method callback
        chatConnection.on("ReceiveMessage", this.messageReceiver::recieve, MessageIn.class);

        //Start the connection
        chatConnection.start().blockingAwait();*/

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