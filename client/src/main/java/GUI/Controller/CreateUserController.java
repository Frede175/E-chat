package GUI.Controller;

import Acquaintence.IRole;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class CreateUserController {


    public TextField PasswordTextField;
    public TextField UsernameTextField;
    public ComboBox<IRole> roleCB;

    public void initialize() {
        roleCB.getItems().addAll(GUI.GUI.getInstance().getBusiness().getRoles().getResponse());
        if(!roleCB.getItems().isEmpty()) {
            roleCB.getSelectionModel().select(0 );
        }
    }

    public void createUser(ActionEvent actionEvent) {
        GUI.GUI.getInstance().getBusiness().createUser(UsernameTextField.getText(), PasswordTextField.getText(), roleCB.getSelectionModel().getSelectedItem());
    }
}
