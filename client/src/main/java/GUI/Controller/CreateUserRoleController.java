package GUI.Controller;

import Acquaintence.IDepartment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.List;

public class CreateUserRoleController {
    @FXML
    public ListSelectionView<String> roleListLSV;

    @FXML
    public Button createRole;

    @FXML
    public TextField roleName;

    public void initialize() {
        //TODO Fix this
        roleListLSV.getSourceItems().addAll(GUI.GUI.getInstance().getBusiness().getAllPermissions().getResponse());
    }

    public void createRole(ActionEvent actionEvent) {
        List<String> permissionsForRole = new ArrayList<>();
        for(String permission : roleListLSV.getTargetItems()) {
            permissionsForRole.add(permission);
        }
        GUI.GUI.getInstance().getBusiness().createUserRole(permissionsForRole, roleName.getText());
    }

}
