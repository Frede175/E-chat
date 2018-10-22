package client;

public class MessageReceiver implements IMessageReceiver {
    @Override
    public void receive(MessageObject message) {
        System.out.println(message.getMessage() + " - " + message.getUser());
    }
}
