package GUI.Controller;

import Acquaintence.IRole;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class DeleteRoleController {

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button deleteRole;

    private String selectedRole;

    public void initialize() {

        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role);
        }
        if(!selectRole.getItems().isEmpty()) {
            selectRole.getSelectionModel().select(0);
            selectedRole = selectRole.getItems().get(0).getId();
        }
        selectRole.valueProperty().addListener(new ChangeListener<IRole>() {
            @Override
            public void changed(ObservableValue<? extends IRole> observableValue, IRole iRole, IRole t1) {
                selectedRole = t1.getId();
            }

        });

    }

    public void deleteRole(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().deleteRole(selectedRole);
        Stage stage = (Stage) deleteRole.getScene().getWindow();
        stage.close();
    }
}
