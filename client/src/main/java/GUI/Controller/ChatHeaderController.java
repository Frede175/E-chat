package GUI.Controller;


import Acquaintence.Event.ChangeChatEvent;
import Acquaintence.EventManager;
import Acquaintence.IChat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class ChatHeaderController {


    @FXML
    private Label chatNameL;
    @FXML
    private Button cHButton;
    @FXML
    private Button leaveBtn;
    PopOver popover = new PopOver();

    public void initialize() {
        EventManager.getInstance().registerListener(ChangeChatEvent.class, this::changeName);
        // Changes the name to the name of the current chat on start up
        IChat currentChat = GUI.GUI.getInstance().getBusiness().getCurrentChat();
        if(currentChat != null) {
            chatNameL.setText(currentChat.getName());
            showBtns(true);
        } else {
            chatNameL.setText("");
            showBtns(false);
        }


    }

    private void showBtns(boolean show) {
        cHButton.setVisible(show);
        leaveBtn.setVisible(show);
    }

    public void leaveChat(){
        GUI.GUI.getInstance().getBusiness().leaveChat(GUI.GUI.getInstance().getBusiness().getCurrentChat().getId());
    }

    private void changeName(ChangeChatEvent event) {
        if(event.getChat() != null) {
            Platform.runLater(() -> {
                chatNameL.setText(event.getChat().getName());
                showBtns(true);
            });
        } else {
            Platform.runLater(() -> {
                chatNameL.setText("");
                showBtns(false);
            });
        }
    }

    public void usersInChatBtn(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/UsersInChat.fxml"));

        popover.setAutoFix(true);
        popover.setAutoHide(true);
        popover.setHideOnEscape(true);
        popover.setDetachable(false);
        popover.setContentNode(root);

        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.show(cHButton);
    }

}

