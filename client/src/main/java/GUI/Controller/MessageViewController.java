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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tooltip;

import java.util.*;

public class MessageViewController {

    @FXML
    public ListView<IMessageIn> messageView;

    private ObservableList<IMessageIn> messages;
    private SortedList<IMessageIn> sortedMessages;

    private boolean canGetMore = true;

    @FXML
    public void initialize() {
        // Registers the event listener
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);
        EventManager.getInstance().registerListener(ChangeChatEvent.class, this::changeChat);

        messages = FXCollections.observableArrayList();
        sortedMessages = new SortedList<>(messages, Comparator.comparing(IMessageIn::getTimeStamp));
        messageView.setItems(sortedMessages);

        messageView.setCellFactory(param -> new ListCell<IMessageIn>() {
            @Override
            protected void updateItem(IMessageIn item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getUser().getName() + " | " + item.getContent());
                }
                setOnMouseEntered(event -> {
                    if (empty || item == null){
                        setText(null);
                        setGraphic(null);
                    } else {
                        Tooltip tt = new Tooltip("" + item.getTimeStamp());
                        setTooltip(tt);
                    }

                });
            }
        });
        // Scrolls to the newest message
        Platform.runLater( () -> messageView.scrollTo(messages.size()-1) );
        Platform.runLater(() -> {
            ScrollBar bar  = (ScrollBar) messageView.lookup(".scroll-bar:vertical");
            bar.valueProperty().addListener((observableValue, number, t1) -> {
                if (number.doubleValue() != 0 && t1.doubleValue() == 0 && canGetMore) {
                    int page = messages.size()  / 20;
                    RequestResponse<List<? extends IMessageIn>> response = GUI.getInstance().getBusiness().getMessages(page);
                    List<? extends IMessageIn> newMessages = response.getResponse();
                    newMessages.removeAll(messages);
                    if (!newMessages.isEmpty()) {
                        messages.addAll(newMessages);
                        messageView.scrollTo(response.getResponse().get(0));
                    } else {
                        canGetMore = false;
                    }
                }
            });
        });
    }

    // The event listener method for new message
    private void getMessage(MessageEvent messageEvent) {
        if(GUI.getInstance().getBusiness().getCurrentChat() != null && messageEvent.getMessageIn().getChatId() == GUI.getInstance().getBusiness().getCurrentChat().getId()) {
            Platform.runLater(() -> {
                IMessageIn message = messageEvent.getMessageIn();
                messages.add(message);
            });
        }
    }

    // The event listener method for change chat
    private void changeChat(ChangeChatEvent changeChatEvent) {
        IChat chat = changeChatEvent.getChat();
        if(chat != null && chat.getMessages() != null){
            Platform.runLater(() -> {
                List<? extends IMessageIn> mes = new ArrayList<>(chat.getMessages());
                messages.setAll(mes);
            });
        } else {
            Platform.runLater(() -> {
                messages.clear();
            });
        }

    }

    // Gets the messages upon start
    public void getMessages(){
        if(GUI.getInstance().getBusiness().getCurrentChat() != null) {
            Set<? extends IMessageIn> response  = GUI.getInstance().getBusiness().getCurrentChat().getMessages();
            if(response != null){
                messages.addAll(response);
            }
        }
    }
}