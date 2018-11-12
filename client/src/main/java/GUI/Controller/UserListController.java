package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IUser;
import Business.Connection.RequestResponse;
import Business.Models.User;
import GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class UserListController {

    @FXML
    private ListView<String> userList;

    private RequestResponse<List<? extends IUser>> response;

    public void initialize() {
        ArrayList<String> stringList = new ArrayList<>();

        response = GUI.getInstance().getBusiness().getUsers();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            System.out.println("Not null");
            for (IUser user : response.getResponse()) {
                stringList.add(user.getName());
            }
            stringList.add("outofbounds");

        }
        userList.setPrefWidth(100);
        userList.setPrefHeight(70);
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        userList.setItems(names);
        userList.setCellFactory(ComboBoxListCell.forListView(names));
        userList.getSelectionModel().selectFirst();

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + userList.getSelectionModel().getSelectedItem());
                User temp = null;
                for(IUser user : response.getResponse()) {
                    //TODO fix sub/id
                    System.out.println(user.getName() + "'s sub is " + user.getSub());
                    if(user.getName().equals(userList.getSelectionModel().getSelectedItem())) {
                        System.out.println("Changed to: " + user.getName());
                        temp = (User) user;
                    }
                }
                System.out.println("Temp = " + temp.getName());
                GUI.getInstance().getBusiness().createDirectMessage(userList.getSelectionModel().getSelectedItem(), temp);

            }
        });
    }
}