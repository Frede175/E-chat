package GUI.Controller;

import Acquaintence.IChat;
import Business.Connection.RequestResponse;
import Business.Models.Chat;
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

    public void getData() {
        ArrayList<String> stringList = new ArrayList<>();

        RequestResponse<List<IChat>> response = GUI.GUI.getInstance().getBusiness().getChats();
        List<IChat> chats = response.getResponse();
        System.out.println(chats.get(0).getID());
        if(chats != null) {
            System.out.println("Not null");
            for (IChat chat : chats) {
                stringList.add(chat.getName());
            }
        stringList.add("element");
            stringList.add("noget");
            stringList.add("something");


        }
        chatList.setPrefWidth(100);
        chatList.setPrefHeight(70);
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        chatList.setItems(names);
        chatList.setCellFactory(ComboBoxListCell.forListView(names));
        chatList.getSelectionModel().selectFirst();


        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println((chatList.getSelectionModel().getSelectedIndex()));
            }
        });


    }

}
