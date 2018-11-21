package Business.Connection;

import Acquaintence.Event.AddChatEvent;
import Acquaintence.Event.MessageEvent;
import Acquaintence.Event.NewChatEvent;
import Acquaintence.EventManager;
import Business.BusinessFacade;
import Business.Models.Chat;
import Business.Models.MessageIn;
import Business.Models.MessageOut;
import Business.Models.User;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;

public class HubConnect {

    private HubConnection chatConnection;

    public void connect(String token) {
        chatConnection = HubConnectionBuilder.create("https://localhost:5001/hubs/chat")
                .withAccessTokenProvider(Single.just(token))
                .build();

        //Add message receive method callback
        chatConnection.on("ReceiveMessage", this::receive, MessageIn.class);

        // New Chat method callback
        chatConnection.on("NewChat", this::newChat, Chat.class);

        // New Chat method callback
        chatConnection.on("Add", this::add, Integer.class, User.class);

        // New Chat method callback
        chatConnection.on("Remove", this::remove, Integer.class, User.class);

        // New Chat method callback
        chatConnection.on("Leave", this::leave, Integer.class, User.class);

        //Start the connection
        chatConnection.start().blockingAwait();
    }

    private void newChat(Chat chat) {
        // TODO Add the chat to the business and maybe throw event, just like when u create a chat via rest
        EventManager.getInstance().fireEvent(new NewChatEvent(this, chat));
    }

    private void add(int chatId, User user) {
        // TODO Check if the chat already exists, else get the chat via rest. If the chat exists add the user to the user list
        EventManager.getInstance().fireEvent(new AddChatEvent(this, chatId, user));
    }

    private void remove(int chatId, User user) {
        // TODO Check if the user is the logged in, if it is remove the chat. Else remove the user from the chats user list
    }

    private void leave(int chatId, User user) {
        // TODO Check if the user is the logged in, if it is remove the chat. Else remove the user from the chats user list
    }

    public void disconnect() {
        chatConnection.stop().blockingAwait();
    }

    public void sendMessage(String message, int chatId) {
        chatConnection.send("SendMessage", new MessageOut(message, chatId));
    }

    private void receive(MessageIn message) {
        EventManager.getInstance().fireEvent(new MessageEvent(this, message));
    }


}
