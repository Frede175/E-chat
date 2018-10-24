package client;


import Business.Models.Chat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Gson gson = new Gson();

        Chat chat = new Chat(2, "Hello");


        String json = gson.toJson(chat);


        Type c = new TypeToken<Chat>(){}.getType();

        Chat newChat = gson.fromJson(json, c);

        System.out.println(newChat.getID() + ", " + newChat.getName());




        Client client = new Client(new MessageReceiver());

        Scanner scanner = new Scanner(System.in);


        String input;
        while (true) {
            System.out.println("s: send, q: quit");
            input = scanner.nextLine();

            switch (input) {
                case "q":
                    client.close();
                    System.exit(0);
                    break;
                case "s":
                    System.out.print("What to send? ");
                    String message = scanner.nextLine();
                    client.send(message);
                    break;
            }

        }
    }
}
