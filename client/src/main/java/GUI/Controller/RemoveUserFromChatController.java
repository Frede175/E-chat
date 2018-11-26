package GUI.Controller;

import Acquaintence.IChat;
import Acquaintence.IUser;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.List;

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
                List<IChat> chats = GUI.getInstance().getBusiness().getUsersChats(selectedUser.getId());
                for (IChat chat : chats) {
                    if(chat.isGroupChat()) {
                        selectChat.getItems().add(chat);
                    }
                }
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
        Stage stage = (Stage) selectUser.getScene().getWindow();
        stage.close();
    }
}
