package GUI.Controller;


import Acquaintence.Event.AddChatEvent;
import Acquaintence.Event.ChangeChatListEvent;
import Acquaintence.Event.NewChatEvent;
import Acquaintence.EventManager;
import Acquaintence.IChat;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;

import java.util.EventObject;


public class ChatListController {


    @FXML
    public ListView<IChat> chatList;

    private ObservableList<IChat> names;


    public <T extends EventObject> void initialize() {
        EventManager.getInstance().registerListener(ChangeChatListEvent.class, this::changeChatList);
        EventManager.getInstance().registerListener(NewChatEvent.class, this::getNewChat);
        EventManager.getInstance().registerListener(AddChatEvent.class, this::getAddChat);
        names = FXCollections.observableArrayList();
        names.addAll(GUI.GUI.getInstance().getBusiness().getExistingChats());
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));
        chatList.getSelectionModel().selectFirst();
        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GUI.GUI.getInstance().getBusiness().setCurrentChat(chatList.getSelectionModel().getSelectedItem().getId());
            }
        });
    }

    private void getAddChat(AddChatEvent addChatEvent) {
        Platform.runLater(() -> {
            names.setAll(GUI.GUI.getInstance().getBusiness().getExistingChats());
        });
    }

    // The event listener method for change chat
    private void changeChatList(ChangeChatListEvent changeChatListEvent) {
        names.add(changeChatListEvent.getChat());
    }

    private void getNewChat(NewChatEvent newChatEvent) {
        Platform.runLater(() -> {
            names.add(newChatEvent.getChat());
        });
    }
}
