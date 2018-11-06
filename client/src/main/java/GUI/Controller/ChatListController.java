package GUI.Controller;


import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Business.Connection.RequestResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import java.util.ArrayList;
import java.util.List;


public class ChatListController {


    @FXML
    public ListView<String> chatList;

    public void getData() {
        ArrayList<String> stringList = new ArrayList<>();
        chatList = new ListView<>();
        RequestResponse<List<? extends IChat>> response = GUI.GUI.getInstance().getBusiness().getChats();
        if(response.getConnectionState() == ConnectionState.SUCCESS) {
            System.out.println("Not null");
            for (IChat chat : response.getResponse()) {
                stringList.add(chat.getName());
            }
        }
        chatList.setPrefWidth(100);
        chatList.setPrefHeight(70);
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));

    }

}
