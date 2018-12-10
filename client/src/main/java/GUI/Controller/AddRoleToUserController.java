package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IRole;
import Acquaintence.IUser;
import GUI.NotificationUpdater;
import GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AddRoleToUserController extends Controller<AdminPageController>{

    @FXML
    ComboBox<IUser> selectUser;

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button addRoleToUser;

    public void initialize() {
        selectUser.valueProperty().addListener((observableValue, iUser, t1) -> {
            selectRole.getItems().setAll(GUI.getInstance().getBusiness().getAvailableRoles(t1.getId()).getResponse());
        });
    }

    @Override
    public void loaded() {
        selectUser.getItems().setAll(parent.getAllUsers());
    }

    public void addSelected(javafx.event.ActionEvent actionEvent) {
        ConnectionState connectionState = GUI.getInstance().getBusiness().addRoleToUser(selectUser.getValue().getId(), selectRole.getValue().getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully added role " + selectRole.getValue().getName() + " to the user " + selectUser.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
