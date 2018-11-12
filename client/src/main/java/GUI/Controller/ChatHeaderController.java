package GUI.Controller;

import GUI.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class ChatHeaderController {

    @FXML
    private Label chatNameL;

    public void usersInChat(ActionEvent actionEvent) throws IOException {
        PopUpWindow.createUserPopUp("/fxml/CreateUser.fxml");
    }
}
