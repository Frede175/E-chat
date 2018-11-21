package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IUser;
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

        //choiceBoxUser.getItems().addAll(GUI.GUI.getInstance().getBusiness())
    }

}
