package Business.Connection;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import Acquaintence.IBusinessFacade;
import Acquaintence.IGUINotifier;
import Business.BusinessFacade;
import Business.Models.MessageIn;
import Business.Models.MessageOut;
import client.MessageObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;

import java.util.EventObject;

public class HubConnect {

    private HubConnection chatConnection;
    private IGUINotifier guiNotifier;
    private BusinessFacade businessFacade;

    public void injectGUINotifier(IGUINotifier guiNotifier) {
        this.guiNotifier = guiNotifier;
    }



    public void injectBusinessFacade(BusinessFacade businessFacade) {
        this.businessFacade = businessFacade;
    }

    public void connect(String token) {
        chatConnection = HubConnectionBuilder.create("https://localhost:5001/hubs/chat")
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
