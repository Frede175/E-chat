package GUI.Controller;

import Acquaintence.ConnectionState;
import GUI.NotificationUpdater;
import Acquaintence.IRole;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.List;

public class RemovePermissionFromRoleController {
    
    @FXML
    public ComboBox<IRole> selectRole;
    
    @FXML
    public ListSelectionView<String> permissionLSV;

    private IRole selectedRole;

    public void initialize() {
        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role);
        }

        selectRole.valueProperty().addListener(new ChangeListener<IRole>() {
            @Override
            public void changed(ObservableValue<? extends IRole> observableValue, IRole iRole, IRole t1) {
                // TODO set getname() to getID();
                selectedRole = t1;
                permissionLSV.getSourceItems().addAll(GUI.getInstance().getBusiness().getRolesPermissions(selectedRole.getId()));
            }

        });


    }

    public void removeSelected(ActionEvent actionEvent) {
        List<String> permissions = new ArrayList<>();
        for(String permission : permissionLSV.getTargetItems()) {
            permissions.add(permission);
        }
        if(selectedRole != null && permissions != null) {
            ConnectionState connectionState = GUI.getInstance().getBusiness().removePermissionsFromRole(selectedRole.getId(), permissions);
            String input = "Succesfully removed the permissions from the role " + selectedRole.getName();
            NotificationUpdater.getInstance().showNotification(input, connectionState);
        }
        Stage stage = (Stage) selectRole.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
    }
}
