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

public class DeleteUserRoleController {

    @FXML
    ComboBox<String> selectRole;

    @FXML
    Button deleteRole;

    private String selectedRole;

    public void initialize() {
        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role.getName());
        }
        if(!selectRole.getItems().isEmpty()) {
            selectRole.getSelectionModel().select(0);
            selectedRole = selectRole.getItems().get(0);
        }
        selectRole.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedRole = t1;
            }
        });

    }

    public void deleteRole(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().deleteRole(selectedRole);
        Stage stage = (Stage) deleteRole.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
    }
}
