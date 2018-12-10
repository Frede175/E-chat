package GUI.Controller;

import GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class LoggedInUserController {

    @FXML
    private Button adminBtn;
    @FXML
    private Label userNameL;


    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());

        if (GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions().isEmpty()) {
            adminBtn.setVisible(false);
        }
    }

    public void openAdmin() {
        GUI.getInstance().loadScene("/fxml/AdminPage.fxml");
    }

    public void logout() {
        GUI.getInstance().getBusiness().logout();
    }
}
