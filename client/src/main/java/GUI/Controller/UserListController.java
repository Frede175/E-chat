package GUI.Controller;

import Acquaintence.*;
import Acquaintence.Event.AddUserEvent;
import Acquaintence.Event.ChangeChatListEvent;
import Acquaintence.Event.LeaveChatEvent;
import Acquaintence.Event.RemoveUserFromChatEvent;
import Business.Connection.RequestResponse;
import Business.Models.User;
import GUI.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class UserListController {

    @FXML
    private ListView<IUser> userList;

    private ObservableList<IUser> names;

    public void initialize() {
        EventManager.getInstance().registerListener(AddUserEvent.class, this::addedUser);
        EventManager.getInstance().registerListener(RemoveUserFromChatEvent.class, this::removeUserFromChat);
        EventManager.getInstance().registerListener(LeaveChatEvent.class, this::leaveChatEvent);
        names = FXCollections.observableArrayList();
        if(GUI.getInstance().getBusiness().getExistingUsers() != null) {
            names.addAll(GUI.getInstance().getBusiness().getExistingUsers());
        }
        userList.setItems(names);
        userList.setCellFactory(ComboBoxListCell.forListView(names));
        userList.getSelectionModel().selectFirst();

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                IUser temp = null;
                for(IUser user : names) {
                    if(user.getName().equals(userList.getSelectionModel().getSelectedItem().getName())) {
                        temp = user;
                    }
                }

                RequestResponse<? extends IChat> response = GUI.getInstance().getBusiness().createDirectMessage(userList.getSelectionModel().getSelectedItem().getName(), temp);
            }
        });
    }

    private void leaveChatEvent(LeaveChatEvent leaveChatEvent) {
        Platform.runLater(() -> {
            names.setAll(GUI.getInstance().getBusiness().getExistingUsers());
        });
    }

    private void removeUserFromChat(RemoveUserFromChatEvent removeUserFromChatEvent) {
        Platform.runLater(() -> {
            names.setAll(GUI.getInstance().getBusiness().getExistingUsers());
        });
    }

    private void addedUser(AddUserEvent addUserEvent) {
        Platform.runLater(() -> {
            names.addAll(addUserEvent.getUser());
        });
    }
}