package GUI.Controller;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import Acquaintence.IMessageIn;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageViewController {

    @FXML
    public ListView<IMessageIn> chatBox;

    private ObservableList<IMessageIn> messages;
    private SortedList<IMessageIn> sortedMessages;

    @FXML
    public void initialize() {
        // Registers the event listener
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);

        messages = FXCollections.observableArrayList();
        sortedMessages = new SortedList<>(messages);

        chatBox.setItems(sortedMessages);

        chatBox.setCellFactory(param -> new ListCell<IMessageIn>() {
            @Override
            protected void updateItem(IMessageIn item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) return;

                setText(item.getTimeStamp() + " | " + item.getUser().getName() + " | " + item.getContent());
            }
        });
        // Scrolls to the newest message
        Platform.runLater( () -> chatBox.scrollTo(messages.size()-1) );
    }

    // The event listener method
    private void getMessage(MessageEvent messageEvent) {
        Platform.runLater(() -> {
            IMessageIn message = messageEvent.getMessageIn();
            messages.add(message);
        });
    }

    // Gets the messages upon start
    public void getMessages(){
        RequestResponse<List<? extends IMessageIn>> response  = GUI.getInstance().getBusiness().getMessages();
        if(response != null){
            List<? extends IMessageIn> mes = response.getResponse();
            Collections.reverse(mes);
            messages.addAll(mes);
        }
    }
}