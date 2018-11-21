package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IRole;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.List;

public class RemovePermissionFromRoleController {
    
    @FXML
    public ComboBox selectRole;
    
    @FXML
    public ListSelectionView<String> permissionLSV;

    private String selectedRole;

    public void initialize() {
        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role.getName());
        }

        selectRole.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedRole = t1;
                //TODO Fix så den tager den en roles permissions
                permissionLSV.getSourceItems().addAll(GUI.getInstance().getBusiness().getRolesPermissions(selectedRole));
            }
        });


    }

    public void removeSelected(ActionEvent actionEvent) {
        List<String> permissions = new ArrayList<>();
        for(String permission : permissionLSV.getTargetItems()) {
            System.out.println("Adding " + permission);
            permissions.add(permission);
        }
        if(selectedRole != null && permissions != null) {
            GUI.getInstance().getBusiness().removePermissionsFromUser(selectedRole, permissions);
        }

    }
}