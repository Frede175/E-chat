package GUI.Controller;

import Acquaintence.IDepartment;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class AddUserToDepartmentController {

    public ChoiceBox<IDepartment> choiceBoxDepartment;
    public ChoiceBox<IUser> choiceBoxUser;
    public Button addBtn;


    public void initialize(){

        choiceBoxUser.getItems().setAll(GUI.getInstance().getBusiness().getUsers().getResponse());
        if(!choiceBoxUser.getItems().isEmpty()){
            choiceBoxUser.getSelectionModel().select(0);
        }
        choiceBoxUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observableValue, IUser iUser, IUser t1) {
                choiceBoxDepartment.getItems().setAll(GUI.getInstance().getBusiness().getAvailableDepartments(t1.getId()).getResponse());
            }
        });
    }

    public void addToDepartmentBtn(ActionEvent actionEvent){
        GUI.getInstance().getBusiness().addUserToDepartment(choiceBoxDepartment.getValue().getId(), choiceBoxUser.getValue().getId());
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
    }

}
