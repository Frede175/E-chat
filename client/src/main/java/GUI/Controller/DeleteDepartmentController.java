package GUI.Controller;

import Acquaintence.IDepartment;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;


public class DeleteDepartmentController {

    public ChoiceBox<IDepartment> choiceBox;
    public Button deleteBtn;


    public void initialize(){

        choiceBox.getItems().addAll(GUI.GUI.getInstance().getBusiness().getAllDepartments().getResponse());
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getSelectionModel().select(0);
        }

    }

    public void delete(ActionEvent actionEvent){
        GUI.GUI.getInstance().getBusiness().deleteDepartment(choiceBox.getValue().getId());
        Stage stage = (Stage) deleteBtn.getScene().getWindow();
        stage.setScene(GUI.GUI.getInstance().getPrimaryScene());
    }


}
