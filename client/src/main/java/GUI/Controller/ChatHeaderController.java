package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChatHeaderController {

    @FXML
    private Label chatNameL;

    public void usersInChat(ActionEvent actionEvent) {
        chatNameL.setText("Lars er mega nice og sej");
    }
}
