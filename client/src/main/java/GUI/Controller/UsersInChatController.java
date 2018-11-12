package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Acquaintence.IUser;
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

    private ObservableList<String> meh;

    public void initializa(){
        meh = FXCollections.observableArrayList();
        meh.add("Lars er cool nok.. lidt træls med bajer, men stadig.");
        Userlist.setItems(meh);
    }



    public void setUserlist() {
        ArrayList<String> stringList = new ArrayList<>();

        RequestResponse<List<? extends IUser>> response = GUI.GUI.getInstance().getBusiness().getUsersInChat();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            System.out.println("Not null");
            for (IUser user : response.getResponse()) {
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
