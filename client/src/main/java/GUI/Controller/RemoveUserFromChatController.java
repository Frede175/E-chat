package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Acquaintence.IUser;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.List;

public class RemoveUserFromChatController extends Controller<AdminPageController> {
    @FXML
    public ComboBox<IUser> selectUser;
    @FXML
    public ComboBox<IChat> selectChat;

    private IUser selectedUser;
    private IChat selectedChat;

    public void initialize() {
        selectUser.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = newValue;
            List<? extends IChat> chats = GUI.getInstance().getBusiness().getUsersChats(selectedUser.getId());
            for (IChat chat : chats) {
                if(chat.isGroupChat()) {
                    selectChat.getItems().add(chat);
                }
            }
        });

        selectChat.valueProperty().addListener((observableValue, iChat, t1) -> selectedChat = t1);

    }

    @Override
    public void loaded() {
        selectUser.getItems().addAll(parent.getAllUsers());
    }

    public void removeUserFromChat(ActionEvent actionEvent) {
        ConnectionState connectionState = GUI.getInstance().getBusiness().removeUserFromChat(selectedChat.getId(), selectedUser.getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully removed the user " + selectedUser.getName() + " from the chat " + selectedChat.getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
