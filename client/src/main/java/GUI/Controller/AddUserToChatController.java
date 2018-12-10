package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Acquaintence.IUser;
import GUI.NotificationUpdater;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AddUserToChatController extends Controller<AdminPageController> {
    @FXML
    public ComboBox<IChat> chatsCB;
    @FXML
    public ComboBox<IUser> usersCB;

    public void initialize() {
        usersCB.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observable, IUser oldValue, IUser newValue) {
                chatsCB.getItems().setAll(GUI.getInstance().getBusiness().getAvailableChats(newValue.getId()));
            }
        });
    }

    @Override
    public void loaded() {
        usersCB.getItems().addAll(parent.getAllUsers());
    }

    public void addUser() {
        ConnectionState connectionState = GUI.getInstance().getBusiness().addUserToChat(chatsCB.getSelectionModel().getSelectedItem().getId(), usersCB.getSelectionModel().getSelectedItem().getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully added the user " + usersCB.getSelectionModel().getSelectedItem().getName() +
                        " to the chat " + chatsCB.getSelectionModel().getSelectedItem().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }


}
