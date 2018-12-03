package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IRole;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.List;

public class RemovePermissionFromRoleController {
    
    @FXML
    public ComboBox<IRole> selectRole;
    
    @FXML
    public ListSelectionView<String> permissionLSV;

    private String selectedRole;

    public void initialize() {
        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role);
        }

        selectRole.valueProperty().addListener(new ChangeListener<IRole>() {
            @Override
            public void changed(ObservableValue<? extends IRole> observableValue, IRole iRole, IRole t1) {
                // TODO set getname() to getID();
                selectedRole = t1.getName();
                permissionLSV.getSourceItems().addAll(GUI.getInstance().getBusiness().getRolesPermissions(selectedRole));
            }

        });


    }

    public void removeSelected(ActionEvent actionEvent) {
        List<String> permissions = new ArrayList<>();
        for(String permission : permissionLSV.getTargetItems()) {
            permissions.add(permission);
        }
        if(selectedRole != null && permissions != null) {
            GUI.getInstance().getBusiness().removePermissionsFromRole(selectedRole, permissions);
        }
        Stage stage = (Stage) selectRole.getScene().getWindow();
        stage.close();
    }
}
