package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IUser;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class AddUserToDepartmentController {

    public ChoiceBox<IDepartment> choiceBoxDepartment;
    public ChoiceBox<IUser> choiceBoxUser;
    public Button addBtn;


    public void initialize(){

        choiceBoxDepartment.getItems().addAll(GUI.GUI.getInstance().getBusiness().getAllDepartments().getResponse());
        if(!choiceBoxDepartment.getItems().isEmpty()){
            choiceBoxDepartment.getSelectionModel().select(0);
        }

        choiceBoxUser.getItems().addAll(GUI.GUI.getInstance().getBusiness().getUsers().getResponse());
        if(!choiceBoxUser.getItems().isEmpty()){
            choiceBoxUser.getSelectionModel().select(0);
        }
    }

    public void addToDepartmentBtn(ActionEvent actionEvent){

        GUI.GUI.getInstance().getBusiness().addUserToDepartment(choiceBoxDepartment.getValue().getId(), choiceBoxUser.getValue().getId());
    }

}
