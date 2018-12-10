package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IDepartment;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateDepartmentController extends Controller<AdminPageController> {

    public ChoiceBox<IDepartment> choiceBox;
    public TextField txTField;
    public Button renameBtn;

    @Override
    public void loaded() {
        choiceBox.getItems().addAll(parent.getAllDepartments());
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getSelectionModel().select(0);
        }
    }


    public void rename(ActionEvent actionEvent){
        ConnectionState connectionState = GUI.getInstance().getBusiness().updateDepartment(choiceBox.getValue().getId(), txTField.getText());
        String input = "Succesfully renamed the department " + choiceBox.getValue().getName() + " to the name " + txTField.getText();
        NotificationUpdater.getInstance().showNotification(input, connectionState);

        GUI.getInstance().loadMainScene();
    }
}
