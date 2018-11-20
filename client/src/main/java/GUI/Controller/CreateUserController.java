package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IRole;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ListSelectionView;

import java.util.ArrayList;

public class CreateUserController {


    public TextField PasswordTextField;
    public TextField UsernameTextField;
    public ListSelectionView<IDepartment> departments;
    public VBox root;
    public ComboBox<IRole> roleCB;

    public void initialize() {
        roleCB.getItems().addAll(GUI.GUI.getInstance().getBusiness().getRoles().getResponse());
        if(!roleCB.getItems().isEmpty()) {
            roleCB.getSelectionModel().select(0 );
        }
        departments = new ListSelectionView<>();
        departments.getSourceItems().addAll(GUI.GUI.getInstance().getBusiness().getAllDepartments().getResponse());
        root.getChildren().add(departments);
    }

    public void createUser(ActionEvent actionEvent) {
        ArrayList<Integer> departmentIds = new ArrayList<>();
        for(IDepartment dep : departments.getTargetItems()) {
            departmentIds.add(dep.getId());
        }
        GUI.GUI.getInstance().getBusiness().createUser(UsernameTextField.getText(), PasswordTextField.getText(), roleCB.getSelectionModel().getSelectedItem(), departmentIds);
    }
}
