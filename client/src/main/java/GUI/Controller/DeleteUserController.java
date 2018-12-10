package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import GUI.NotificationUpdater;

public class DeleteUserController extends Controller<AdminPageController> {

    @FXML
    public ComboBox<IUser> selectUser;

    private IUser selectedUser;

    public void initialize() {
        selectUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observableValue, IUser s, IUser t1) {
                selectedUser = t1;
            }
        });

    }

    @Override
    public void loaded() {
        selectUser.getItems().addAll(parent.getAllUsers());
    }

    public void deleteUser(ActionEvent actionEvent) {
        ConnectionState connectionState = GUI.getInstance().getBusiness().deleteUser(selectedUser.getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully deleted the user " + selectedUser.getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }


}
