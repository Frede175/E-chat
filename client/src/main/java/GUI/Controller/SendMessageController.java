package GUI.Controller;

import GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SendMessageController {

    @FXML
    private TextField messageTF;

    public void sendMessage(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().sendMessage(messageTF.getText());
        messageTF.setText("");
    }
}
