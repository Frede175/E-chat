package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IDepartment;
import Acquaintence.IRole;
import Business.Connection.RestConnect;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;

public class CreateUserController extends Controller<AdminPageController> {


    public TextField PasswordTextField;
    public TextField UsernameTextField;
    public ListSelectionView<IDepartment> departmentsLSV;
    public VBox root;
    public ComboBox<IRole> roleCB;

    @Override
    public void loaded() {
        roleCB.getItems().addAll(parent.getAllRoles());
        if(!roleCB.getItems().isEmpty()) {
            roleCB.getSelectionModel().select(0 );
        }
        departmentsLSV.getSourceItems().addAll(parent.getAllDepartments());
    }

    public void createUser(ActionEvent actionEvent) {
        ArrayList<Integer> departmentIds = new ArrayList<>();
        for(IDepartment dep : departmentsLSV.getTargetItems()) {
            departmentIds.add(dep.getId());
        }
        ConnectionState connectionState = GUI.getInstance().getBusiness().createUser(UsernameTextField.getText(), PasswordTextField.getText(), roleCB.getSelectionModel().getSelectedItem(), departmentIds);

        GUI.getInstance().loadMainScene();
        String input = "User " + UsernameTextField.getText() + " succesfully created";
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
