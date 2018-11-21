package GUI.Controller;

import Acquaintence.IChat;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class RemoveUserFromChatController {
    @FXML
    public ComboBox<IUser> selectUser;
    @FXML
    public ComboBox<IChat> selectChat;

    private IUser selectedUser;
    private IChat selectedChat;

    public void initialize() {
        for (IUser user : GUI.getInstance().getBusiness().getUsers().getResponse()) {
            selectUser.getItems().add(user);
        }
        selectUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observable, IUser oldValue, IUser newValue) {
                selectedUser = newValue;
                selectChat.getItems().setAll(GUI.getInstance().getBusiness().getUsersChats(selectedUser.getId()));
            }
        });

        selectChat.valueProperty().addListener(new ChangeListener<IChat>() {
            @Override
            public void changed(ObservableValue<? extends IChat> observableValue, IChat iChat, IChat t1) {
                selectedChat = t1;
            }

        });

    }

    public void removeUserFromChat(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().removeUserFromChat(selectedChat.getId(), selectedUser.getId());
    }
}
