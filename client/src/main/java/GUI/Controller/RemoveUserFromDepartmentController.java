package GUI.Controller;

import Acquaintence.ConnectionState;
import GUI.NotificationUpdater;
import Acquaintence.IDepartment;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class RemoveUserFromDepartmentController extends Controller<AdminPageController> {
    @FXML
    public ComboBox<IUser> selectUser;
    @FXML
    public ComboBox<IDepartment> selectDepartment;

    private IUser selectedUser;
    private IDepartment selectedDepartment;

    public void initialize() {
        selectDepartment.valueProperty().addListener((observableValue, iDepartment, t1) -> {
            selectedDepartment = t1;
            selectUser.getItems().setAll(GUI.getInstance().getBusiness().getAllUsersInDepartment(selectedDepartment.getId()).getResponse());

        });

        selectUser.valueProperty().addListener((observableValue, iUser, t1) -> selectedUser = t1);

    }

    @Override
    public void loaded() {
        selectDepartment.getItems().addAll(parent.getAllDepartments());
    }

    public void removeUserFromDepartment(ActionEvent actionEvent) {
        ConnectionState connectionState = GUI.getInstance().getBusiness().removeUserFromDepartment(selectedUser.getId(), selectedDepartment.getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully removed the user " + selectedUser.getName() + " from the department " + selectedDepartment.getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
