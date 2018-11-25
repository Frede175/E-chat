package GUI.Controller;

import Acquaintence.Event.ChangeChatEvent;
import Acquaintence.Event.ChangeChatListEvent;
import Acquaintence.EventManager;
import GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.EventObject;

public class SendMessageController {

    @FXML
    private TextField messageTF;
    @FXML
    private Button sendBtn;

    public void initialize() {
        EventManager.getInstance().registerListener(ChangeChatEvent.class, this::changeChat);

        if (GUI.getInstance().getBusiness().getCurrentChat() == null) {
            disable(true);
        }
    }

    private void changeChat(ChangeChatEvent changeChatEvent) {
        if(changeChatEvent.getChat() == null) {
            disable(true);
        } else {
            disable(false);
        }
    }

    private void disable(boolean disable) {
        messageTF.setDisable(disable);
        sendBtn.setDisable(disable);
    }

    public void sendMessage(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().sendMessage(messageTF.getText());
        messageTF.setText("");
    }
}
