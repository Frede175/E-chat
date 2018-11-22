package GUI.Controller;

import Acquaintence.IRole;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddPermissionsToRoleController {

    @FXML
    public ComboBox<String> selectRole;

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
                RequestResponse<List<String>> response1 = GUI.getInstance().getBusiness().getAllPermissions();
                List<String> rolesPermissions = GUI.getInstance().getBusiness().getRolesPermissions(selectedRole);
                List<String> allPermissions = response1.getResponse();
                List<String> notCurrentPermissions = new ArrayList<>();
                for(Iterator<String> iterator = allPermissions.iterator(); iterator.hasNext();) {
                    String perm = iterator.next();
                    if(!rolesPermissions.contains(perm)) {
                        notCurrentPermissions.add(perm);
                        iterator.remove();
                    }
                }
                permissionLSV.getSourceItems().setAll(notCurrentPermissions);
                permissionLSV.getTargetItems().setAll(allPermissions);
            }
        });
    }

    public void addSelected(ActionEvent actionEvent) {
        List<String> permissions = new ArrayList<>();
        permissions.addAll(permissionLSV.getTargetItems());
        if(selectedRole != null) {
            GUI.getInstance().getBusiness().addPermissionsToRole(selectedRole, permissions);
            Stage stage = (Stage) selectRole.getScene().getWindow();
            stage.close();
        }
    }
}
