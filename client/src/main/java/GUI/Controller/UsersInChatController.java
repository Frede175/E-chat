package GUI.Controller;

import Business.Connection.RequestResponse;
import Business.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import java.util.ArrayList;
import java.util.List;

public class UsersInChatController {


    @FXML
    public ListView<String> Userlist;

    public void setUserlist() {
        ArrayList<String> stringList = new ArrayList<>();

        RequestResponse<List<User>> response = GUI.GUI.getInstance().getBusiness().getUsersInChat();
        List<User> users = response.getResponse();
        if (users != null) {
            System.out.println("Not null");
            for (User user : users) {
                stringList.add(user.getName());
            }
        }
        Userlist.setPrefWidth(100);
        Userlist.setPrefHeight(70);
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        Userlist.setItems(names);
        Userlist.setCellFactory(ComboBoxListCell.forListView(names));

    }
}
