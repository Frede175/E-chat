package GUI.Controller;


import Acquaintence.ConnectionState;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CreateDepartmentController extends Controller<AdminPageController> {

    public TextField DepartmentName;
    public Button CreateBtn;

    public void createDep(ActionEvent actionEvent){
        ConnectionState connectionState = GUI.getInstance().getBusiness().createDepartment(DepartmentName.getText());


        GUI.getInstance().loadMainScene();
        String input = "Succesfully created the department " + DepartmentName.getText();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}

