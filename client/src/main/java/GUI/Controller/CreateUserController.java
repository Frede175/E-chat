package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IDepartment;
import Acquaintence.IRole;
import Business.Connection.RestConnect;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;

public class CreateUserController {


    public TextField PasswordTextField;
    public TextField UsernameTextField;
    public ListSelectionView<IDepartment> departmentsLSV;
    public VBox root;
    public ComboBox<IRole> roleCB;

    public void initialize() {
        roleCB.getItems().addAll(GUI.GUI.getInstance().getBusiness().getRoles().getResponse());
        if(!roleCB.getItems().isEmpty()) {
            roleCB.getSelectionModel().select(0 );
        }
        departmentsLSV.getSourceItems().addAll(GUI.GUI.getInstance().getBusiness().getAllDepartments().getResponse());
    }

    public void createUser(ActionEvent actionEvent) {
        ArrayList<Integer> departmentIds = new ArrayList<>();
        for(IDepartment dep : departmentsLSV.getTargetItems()) {
            departmentIds.add(dep.getId());
        }
        ConnectionState connectionState = GUI.GUI.getInstance().getBusiness().createUser(UsernameTextField.getText(), PasswordTextField.getText(), roleCB.getSelectionModel().getSelectedItem(), departmentIds);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(GUI.GUI.getInstance().getPrimaryScene());
        String input = "User " + UsernameTextField.getText() + " succesfully created";
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
