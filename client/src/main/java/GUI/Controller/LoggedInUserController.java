package GUI.Controller;

import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import GUI.PopUpWindow;

public class LoggedInUserController {

    @FXML
    private ComboBox adminCB;

    @FXML
    private Label userNameL;

    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());
        adminCB.getItems().add("User");
        // TODO loop through types of permissions and add each different categories to the cb
        adminCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                PopUpWindow.createUserPopUp();
            }
        });
    }

    public void logout() {
        // TODO Logout user
    }
}
