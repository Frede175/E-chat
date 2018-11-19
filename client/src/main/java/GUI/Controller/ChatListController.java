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

import java.util.List;


public class ChatListController {


    @FXML
    public ListView<String> chatList;

    private ObservableList<String> names;

    public void initialize() {
        EventManager.getInstance().registerListener(ChangeChatListEvent.class, this::changeChatList);
        names = FXCollections.observableArrayList();
        getChats();
        chatList.getSelectionModel().selectFirst();
    }

    public void getChats() {
        RequestResponse<List<? extends IChat>> response = GUI.GUI.getInstance().getBusiness().getChats();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            for (IChat chat : response.getResponse()) {
                names.add(chat.getName());
            }
        }
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));
        chatList.getSelectionModel().selectFirst();
        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GUI.GUI.getInstance().getBusiness().setCurrentChat(response.getResponse().get(chatList.getSelectionModel().getSelectedIndex()).getId());
            }
        });
    }

    // The event listener method for change chat
    private void changeChatList(ChangeChatListEvent changeChatListEvent) {
        names.add(changeChatListEvent.getChat().getName());
    }

}
