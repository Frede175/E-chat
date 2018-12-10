package GUI.Controller;

import Business.Connection.PermissionEnum;
import GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LoggedInUserController {

    @FXML
    private HBox root;
    @FXML
    private Button adminBtn;
    @FXML
    private Label userNameL;


    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());

        if (GUI.getInstance().getBusiness().getLoginUser().getUserPermissions().isEmpty()) {
            //í3vm
            root.setPrefWidth(GUI.getInstance().getStage().getScene().getWidth());
            root.setPrefHeight(GUI.getInstance().getStage().getScene().getHeight());
        }

        if (GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions().isEmpty()) {
            adminBtn.setVisible(false);
        }
    }

    public void openAdmin() {
        GUI.getInstance().loadScene("/fxml/AdminPage.fxml");
    }

    public void logout() {
        GUI.getInstance().getBusiness().logout();
        GUI.getInstance().loadScene("/fxml/Login.fxml");
    }
}
