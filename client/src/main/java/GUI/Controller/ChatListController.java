package GUI.Controller;


import Acquaintence.ConnectionState;
import Acquaintence.Event.ChangeChatListEvent;
import Acquaintence.EventManager;
import Acquaintence.IChat;
import Business.Connection.RequestResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class ChatListController {


    @FXML
    public ListView<String> chatList;

    ArrayList<String> stringList;
    RequestResponse<List<? extends IChat>> response;
    ObservableList<String> names;


    public void initialize() {
        stringList = new ArrayList<>();

        EventManager.getInstance().registerListener(ChangeChatListEvent.class, this::changeChatList);

        chatList.setPrefWidth(100);
        chatList.setPrefHeight(70);
        getChats();
        names = FXCollections.observableArrayList(stringList);
        chatList.getSelectionModel().selectFirst();
        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GUI.GUI.getInstance().getBusiness().setCurrentChat(response.getResponse().get(chatList.getSelectionModel().getSelectedIndex()).getId());
            }
        }
        );

    }

    public void getChats() {

        response = GUI.GUI.getInstance().getBusiness().getChats();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            System.out.println("Not null");
            for (IChat chat : response.getResponse()) {
                stringList.add(chat.getName());
            }
        stringList.add("outofbounds");

        }

        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));

    }

    // The event listener method for change chat
    private void changeChatList(ChangeChatListEvent changeChatListEvent) {
        getChats();
    }

}
