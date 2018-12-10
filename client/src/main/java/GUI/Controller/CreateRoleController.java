package GUI.Controller;

import Acquaintence.ConnectionState;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;
import java.util.List;

public class CreateRoleController extends Controller<AdminPageController> {
    @FXML
    public ListSelectionView<String> roleListLSV;

    @FXML
    public Button createRole;

    @FXML
    public TextField roleName;

    @Override
    public void loaded() {
        roleListLSV.getSourceItems().addAll(parent.getAllPermissions());
    }

    public void createRole(ActionEvent actionEvent) {
        List<String> permissionsForRole = new ArrayList<>();
        permissionsForRole.addAll(roleListLSV.getTargetItems());
        ConnectionState connectionState = GUI.getInstance().getBusiness().createRole(permissionsForRole, roleName.getText());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully created the role " + roleName.getText();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }


}
