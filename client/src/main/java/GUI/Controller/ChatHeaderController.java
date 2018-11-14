package GUI.Controller;

import Acquaintence.Event.ChangeChatEvent;
import Acquaintence.EventManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChatHeaderController {

    @FXML
    private Label chatNameL;

    public void initialize() {
        EventManager.getInstance().registerListener(ChangeChatEvent.class, this::changeName);
        // Changes the name to the name of the current chat on start up
        chatNameL.setText(GUI.GUI.getInstance().getBusiness().getCurrentChat().getName());
    }

    private void changeName(ChangeChatEvent event) {
        chatNameL.setText(event.getChat().getName());
    }

    public void usersInChat(ActionEvent actionEvent) {
        chatNameL.setText("Lars er mega nice og sej");
    }
}
