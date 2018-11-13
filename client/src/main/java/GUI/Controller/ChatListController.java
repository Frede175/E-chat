package GUI.Controller;


import Acquaintence.ConnectionState;
import Acquaintence.Event.ChangeChatEvent;
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

    public void getChats() {
        ArrayList<String> stringList = new ArrayList<>();

        RequestResponse<List<? extends IChat>> response = GUI.GUI.getInstance().getBusiness().getChats();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            for (IChat chat : response.getResponse()) {
                stringList.add(chat.getName());
            }
        }
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));
        chatList.getSelectionModel().selectFirst();


        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // TODO Create check for same chat clicked
                EventManager.getInstance().fireEvent(new ChangeChatEvent(this, response.getResponse().get(chatList.getSelectionModel().getSelectedIndex())));
            }
        });


    }

}
