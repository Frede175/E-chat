package client;

import Business.Connection.RestConnect;
import Business.Models.Department;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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
