package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.List;

public class CreateRoleController {
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
        permissionsForRole.addAll(roleListLSV.getTargetItems());
        GUI.GUI.getInstance().getBusiness().createRole(permissionsForRole, roleName.getText());
        Stage stage = (Stage) createRole.getScene().getWindow();
        stage.close();
    }

}
