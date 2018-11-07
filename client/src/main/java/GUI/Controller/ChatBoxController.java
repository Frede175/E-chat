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

import java.util.ArrayList;
import java.util.List;


public class ChatBoxController{

    @FXML
    public ListView<IMessageIn> chatBox;
    private ObservableList<IMessageIn> messages;// = FXCollections.observableArrayList();
    private SortedList<IMessageIn> sortedMessages;// = new SortedList<IMessageIn>(messages);


    @FXML
    public void initialize() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::getMessage);

        messages = FXCollections.observableArrayList();
        sortedMessages = new SortedList<IMessageIn>(messages);

        chatBox.setItems(sortedMessages);



        chatBox.setCellFactory(param -> new ListCell<IMessageIn>() {
            @Override
            protected void updateItem(IMessageIn item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) return;

                setText(item.getTimeStamp() + " | " + item.getUser().getName() + " | " + item.getContent());
            }
        });


    }

    private void getMessage(MessageEvent messageEvent) {
        System.out.println("Get message called");
        Platform.runLater(() -> {
            System.out.println("Test");
            IMessageIn message = messageEvent.getMessageIn();
            messages.add(message);
        });
    }

    // Takes the content from MessagesIn and shows it in the ListVeiw in the GUI TODO it should work.. but not quite yet
    public void getData(){
        RequestResponse<List<? extends IMessageIn>> response  = GUI.getInstance().getBusiness().getMessages();


        if(response == null){
            //messages.add("There is no messages in this chat");
        }else{
            messages.addAll(response.getResponse());

        }
    }
}