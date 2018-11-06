package GUI.Controller;

import Acquaintence.IChat;
import Business.Connection.RequestResponse;
import Business.Models.Chat;
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
        //Userlist = new ListView<>();

        RequestResponse<List<IChat>> response = GUI.GUI.getInstance().getBusiness().getChats();
        List<IChat> chats = response.getResponse();
        if(chats != null) {
            System.out.println("Not null");
            for (IChat chat : chats) {
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
