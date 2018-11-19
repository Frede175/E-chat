package GUI.Controller;

import Acquaintence.*;
import Acquaintence.Event.ChangeChatListEvent;
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
    private ObservableList<String> names;

    public void initialize() {
        names = FXCollections.observableArrayList();
        response = GUI.getInstance().getBusiness().getUsers();
        if (response.getConnectionState() == ConnectionState.SUCCESS) {
            for (IUser user : response.getResponse()) {
                names.add(user.getName());
            }
        }
        userList.setItems(names);
        userList.setCellFactory(ComboBoxListCell.forListView(names));
        userList.getSelectionModel().selectFirst();

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                IUser temp = null;
                for(IUser user : response.getResponse()) {
                    //TODO fix sub/id
                    if(user.getName().equals(userList.getSelectionModel().getSelectedItem())) {
                        temp = user;
                    }
                }

                RequestResponse<? extends IChat> response = GUI.getInstance().getBusiness().createDirectMessage(userList.getSelectionModel().getSelectedItem(), temp);
                EventManager.getInstance().fireEvent(new ChangeChatListEvent(this, response.getResponse()));
            }
        });
    }
}