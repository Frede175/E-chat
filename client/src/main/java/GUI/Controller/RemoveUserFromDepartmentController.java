package GUI.Controller;

import Acquaintence.IChat;
import Acquaintence.IDepartment;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class RemoveUserFromDepartmentController {
    @FXML
    public ComboBox<IUser> selectUser;
    @FXML
    public ComboBox<IDepartment> selectDepartment;

    private IUser selectedUser;
    private IDepartment selectedDepartment;

    public void initialize() {
        for (IDepartment department : GUI.getInstance().getBusiness().getDepartments().getResponse()) {
            selectDepartment.getItems().add(department);
        }
        selectDepartment.valueProperty().addListener(new ChangeListener<IDepartment>() {
            @Override
            public void changed(ObservableValue<? extends IDepartment> observableValue, IDepartment iDepartment, IDepartment t1) {
                selectedDepartment = t1;
                selectUser.getItems().setAll(GUI.getInstance().getBusiness().getAllUsersInDepartment(selectedDepartment.getId()).getResponse());

            }
        });


        selectUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observableValue, IUser iUser, IUser t1) {
                selectedUser = t1;
            }
        });

    }

    public void removeUserFromDepartment(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().removeUserFromDepartment(selectedUser.getId(), selectedDepartment.getId());
        Stage stage = (Stage) selectUser.getScene().getWindow();
        stage.close();
    }
}
