package GUI.Controller;

import Acquaintence.IRole;
import Acquaintence.IUser;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.List;

public class AddRoleToUserController {

    @FXML
    ComboBox<IUser> selectUser;

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button addRoleToUser;

    public void initialize() {
        selectUser.getItems().setAll(GUI.getInstance().getBusiness().getUsers().getResponse());

        selectUser.valueProperty().addListener((observableValue, iUser, t1) -> {
            selectRole.getItems().setAll(GUI.getInstance().getBusiness().getAvailableRoles(t1.getId()).getResponse());
        });
    }


    public void addSelected(javafx.event.ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().addRoleToUser(selectUser.getValue().getId(), selectRole.getValue().getId());
        Stage stage = (Stage) addRoleToUser.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
    }
}
