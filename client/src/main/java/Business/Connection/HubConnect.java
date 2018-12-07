package Business.Connection;

import Acquaintence.Event.*;
import Acquaintence.EventManager;
import Business.BusinessFacade;
import Business.Models.Chat;
import Business.Models.MessageIn;
import Business.Models.MessageOut;
import Business.Models.User;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import io.reactivex.Single;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

public class HubConnect {

    private HubConnection chatConnection;
    private ImageView img;

    public void connect(String token) {
        img = new ImageView(new Image("img/E-chat.png"));
        img.setFitHeight(40.0);
        img.setFitWidth(40.0);

        chatConnection = HubConnectionBuilder.create(ConnectionConst.HOST + "/hubs/chat")
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

        chatConnection.on("DeleteChat", this::deleteChat, Integer.class);

        //Start the connection
        chatConnection.start().blockingAwait();
    }


    private void newChat(Chat chat) {
        EventManager.getInstance().fireEvent(new NewChatEvent(this, chat));
    }

    private void add(int chatId, User user) {
        EventManager.getInstance().fireEvent(new AddChatEvent(this, chatId, user));
    }

    private void remove(int chatId, User user) {
        EventManager.getInstance().fireEvent(new RemoveUserFromChatEvent(this, chatId, user));
    }

    private void leave(int chatId, User user) {
        EventManager.getInstance().fireEvent(new LeaveChatEvent(this, chatId, user));
    }

    private void deleteChat(int chatId) {
        EventManager.getInstance().fireEvent(new DeleteChatEvent(this, chatId));
    }

    public void disconnect() {
        if(chatConnection != null && chatConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            chatConnection.stop().blockingAwait();
            chatConnection = null;
        }
    }

    public void sendMessage(String message, int chatId) {
        chatConnection.send("SendMessage", new MessageOut(message, chatId));
    }

    private void receive(MessageIn message) {
        EventManager.getInstance().fireEvent(new MessageEvent(this, message));
    }
    
}
