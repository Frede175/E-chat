package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IRole;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;
import GUI.NotificationUpdater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddPermissionsToRoleController extends Controller<AdminPageController>{

    @FXML
    public ComboBox<IRole> selectRoleComboBox;

    @FXML
    public ListSelectionView<String> permissionLSV;

    private IRole selectedRole;

    private List<String> rolesPermissions;

    public void initialize() {
        selectRoleComboBox.valueProperty().addListener(new ChangeListener<IRole>() {
            @Override
            public void changed(ObservableValue<? extends IRole> observableValue, IRole iRole, IRole t1) {
                selectedRole = t1;
                RequestResponse<List<String>> response1 = GUI.getInstance().getBusiness().getAllPermissions();
                rolesPermissions = GUI.getInstance().getBusiness().getRolesPermissions(selectedRole.getId());
                List<String> allPermissions = response1.getResponse();
                List<String> notCurrentPermissions = new ArrayList<>();
                for (Iterator<String> iterator = allPermissions.iterator(); iterator.hasNext(); ) {
                    String perm = iterator.next();
                    if (!rolesPermissions.contains(perm)) {
                        notCurrentPermissions.add(perm);
                        iterator.remove();
                    }
                }
                permissionLSV.getSourceItems().setAll(notCurrentPermissions);
                permissionLSV.getTargetItems().setAll(allPermissions);
            }


        });
    }

    @Override
    public void loaded() {
        selectRoleComboBox.getItems().addAll(parent.getAllRoles());
    }

    public void addSelected(ActionEvent actionEvent) {
        ArrayList<String> permissionstosend = new ArrayList<>();
        permissionstosend.addAll(permissionLSV.getTargetItems());
        permissionstosend.removeAll(rolesPermissions);
        if (selectedRole != null && !permissionLSV.getTargetItems().isEmpty()) {
            ConnectionState connectionState = GUI.getInstance().getBusiness().addPermissionsToRole(selectedRole.getId(), permissionstosend);

            GUI.getInstance().loadMainScene();
            String input = "Succesfully added permissions to the role " + selectedRole.getName();
            NotificationUpdater.getInstance().showNotification(input, connectionState);
        }
    }


}
