package GUI.Controller;

import GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;

import java.io.IOException;

public class LoggedInUserController {


    @FXML
    private Label userNameL;

    @FXML
    private MenuButton adminMB;

    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());
    }

    public void openAdmin() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent p = loader.load(getClass().getResource("/fxml/AdminPage.fxml").openStream());
            Scene scene = new Scene(p);
            GUI.getInstance().getStage().setWidth(1000);
            GUI.getInstance().getStage().setHeight(600);
            GUI.getInstance().getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        GUI.getInstance().getBusiness().logout();
    }
}
