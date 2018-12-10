package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IRole;
import Acquaintence.IUser;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.util.ArrayList;
import java.util.List;

public class RemoveRoleFromUserController extends Controller<AdminPageController> {

    @FXML
    ComboBox<IUser> selectUser;

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button removeRoleFromUser;


    public void initialize(){
        selectUser.valueProperty().addListener((observableValue, iUser, t1) -> {
            List<IRole> newRoles = new ArrayList<>(parent.getAllRoles());
            newRoles.removeAll(GUI.getInstance().getBusiness().getAvailableRoles(t1.getId()).getResponse());
            selectRole.getItems().setAll(newRoles);
        });
    }

    @Override
    public void loaded() {
        selectUser.getItems().addAll(parent.getAllUsers());
    }

    public void removeSelected(ActionEvent actionEvent) {
        ConnectionState connectionState = GUI.getInstance().getBusiness().removeRoleFromUser(selectUser.getValue().getId(), selectRole.getValue().getId());

        GUI.getInstance().loadMainScene();

        String input = "Successfully removed role " + selectRole.getValue().getName() + " from the user " + selectUser.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}