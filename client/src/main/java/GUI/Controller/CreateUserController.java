package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreateUserController {


    public TextField PasswordTextField;
    public TextField UsernameTextField;
    public javafx.scene.control.TextArea TextArea;

    public void createUser(ActionEvent actionEvent) {
        GUI.GUI.getInstance().getBusiness().createUser(UsernameTextField.getText(), PasswordTextField.getText());
    }
}
