package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;
import Business.Models.Chat;
import Business.Models.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class ChatListController {

    private Department department = new Department(1, "jeff");

    @FXML
    public ListView<String> chatList;

    public void getData() {
        ArrayList<String> stringList = new ArrayList<>();
        chatList = new ListView<>();
        RequestResponse<List<? extends IChat>> response = GUI.GUI.getInstance().getBusiness().getChats(department.toMap());
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
