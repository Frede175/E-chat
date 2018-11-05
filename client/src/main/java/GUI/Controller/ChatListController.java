package GUI.Controller;

import Business.Connection.PathEnum;
import Business.Models.Chat;
import Business.Models.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import java.util.ArrayList;
import java.util.List;


public class ChatListController {

    private Department department = new Department(1, "jeff");

    @FXML
    public ListView<String> chatList;

    public void getData() {
        /*ArrayList<String> stringList = new ArrayList<>();
        //chatList = new ListView<>();
        List<Chat> chats = GUI.GUI.getInstance().getBusiness().getChats(department.toMap());
        if(chats != null) {
            System.out.println("Not null");
            for (Chat chat : chats) {
                stringList.add(chat.getName());
            }
        }
        chatList.setPrefWidth(100);
        chatList.setPrefHeight(70);
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));*/

    }

}
