package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IDepartment;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;


public class DeleteDepartmentController extends Controller<AdminPageController> {

    public ChoiceBox<IDepartment> choiceBox;
    public Button deleteBtn;

    @Override
    public void loaded() {
        choiceBox.getItems().addAll(parent.getAllDepartments());
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getSelectionModel().select(0);
        }
    }

    public void delete(ActionEvent actionEvent){
        ConnectionState connectionState = GUI.getInstance().getBusiness().deleteDepartment(choiceBox.getValue().getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully deleted the department " + choiceBox.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }


}
