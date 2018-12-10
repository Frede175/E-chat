package GUI.Controller;

import GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField usernameTF;

    @FXML
    public PasswordField passwordTF;

    @FXML
    public Label errorL;

    @FXML
    public VBox root;

    public void initialize() {
        //Fix for i3vm
        if (GUI.getInstance().getStage() != null) {
            root.setPrefWidth(GUI.getInstance().getStage().getScene().getWidth());
            root.setPrefHeight(GUI.getInstance().getStage().getScene().getHeight());
        }

    }

    @FXML
    public void login() {
        switch (GUI.getInstance().getBusiness().login(usernameTF.getText(), passwordTF.getText())) {
            case SUCCESS:
                GUI.getInstance().loadMainSceneFromLogin();
                break;
            case NO_BASIC_PERMISSIONS:
                GUI.getInstance().loadScene("/fxml/LoggedInUser.fxml");
                break;
            case BAD_REQUEST:
                errorL.setText("Username or password is incorrect");
                break;
            case NO_CONNECTION:
                errorL.setText("No connection to server");
        }
        passwordTF.setText("");
    }
}
