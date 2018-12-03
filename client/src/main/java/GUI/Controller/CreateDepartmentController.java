package GUI.Controller;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CreateDepartmentController {

    public TextField DepartmentName;
    public Button CreateBtn;

    public void createDep(ActionEvent actionEvent){
        GUI.GUI.getInstance().getBusiness().createDepartment(DepartmentName.getText());

        Stage stage = (Stage) CreateBtn.getScene().getWindow();
        stage.setScene(GUI.GUI.getInstance().getPrimaryScene());
    }


}

