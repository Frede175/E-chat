package Business.Connection;

import Business.Models.MessageIn;
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
        //chatConnection.on("ReceiveMessage", this.messageReceiver::recieve, MessageIn.class);

        //Start the connection
        chatConnection.start().blockingAwait();
    }

}
