package Business.Connection;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import Business.BusinessFacade;
import Business.Models.MessageIn;
import Business.Models.MessageOut;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;

public class HubConnect {

    private HubConnection chatConnection;

    public void connect(String token) {
        chatConnection = HubConnectionBuilder.create("https://group3server.azurewebsites.net/hubs/chat")//create("https://localhost:5001/hubs/chat")
                .withAccessTokenProvider(Single.just(token))
                .build();

        //Add message receive method callback
        chatConnection.on("ReceiveMessage", this::receive, MessageIn.class);

        //Start the connection
        chatConnection.start().blockingAwait();
    }

    public void sendMessage(String message, int chatId) {
        chatConnection.send("SendMessage", new MessageOut(message, chatId));
    }

    public void receive(MessageIn message) {
        EventManager.getInstance().fireEvent(new MessageEvent(this, message));
    }

}
