package GUI.Controller;

import Acquaintence.Event.ChangeChatEvent;
import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import Acquaintence.IChat;
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

import java.util.*;

public class MessageViewController {

    @FXML
    public ListView<IMessageIn> chatBox;

    private ObservableList<IMessageIn> messages;
    private SortedList<IMessageIn> sortedMessages;

    @FXML
    public void initialize() {
        // Registers the event listener
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
        EventManager.getInstance().registerListener(ChangeChatEvent.class, this::changeChat);

        messages = FXCollections.observableArrayList();
        sortedMessages = new SortedList<>(messages);

        chatBox.setItems(sortedMessages);

        chatBox.setCellFactory(param -> new ListCell<IMessageIn>() {
            @Override
            protected void updateItem(IMessageIn item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getTimeStamp() + " | " + item.getUser().getName() + " | " + item.getContent());
                }
            }
        });
        // Scrolls to the newest message
        Platform.runLater( () -> chatBox.scrollTo(messages.size()-1) );
    }

    // The event listener method for new message
    private void getMessage(MessageEvent messageEvent) {
        if(messageEvent.getMessageIn().getChatId() == GUI.getInstance().getBusiness().getCurrentChat().getId()) {
            Platform.runLater(() -> {
                IMessageIn message = messageEvent.getMessageIn();
                messages.add(message);
            });
        }
    }

    // The event listener method for change chat
    private void changeChat(ChangeChatEvent changeChatEvent) {
        IChat chat = changeChatEvent.getChat();
        if(chat.getMessages() != null){
            List<? extends IMessageIn> mes = new ArrayList<>(chat.getMessages());
            messages.setAll(mes);
        }

    }

    // Gets the messages upon start
    public void getMessages(){
        RequestResponse<List<? extends IMessageIn>> response  = GUI.getInstance().getBusiness().getMessages();
        if(response != null){
            List<? extends IMessageIn> mes = response.getResponse();
            Collections.sort(mes);
            messages.addAll(mes);
        }
    }
}