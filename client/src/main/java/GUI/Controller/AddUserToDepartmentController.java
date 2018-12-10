package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IDepartment;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import GUI.NotificationUpdater;

public class AddUserToDepartmentController extends Controller<AdminPageController> {

    public ChoiceBox<IDepartment> choiceBoxDepartment;
    public ChoiceBox<IUser> choiceBoxUser;
    public Button addBtn;


    public void initialize(){
        choiceBoxUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observableValue, IUser iUser, IUser t1) {
                choiceBoxDepartment.getItems().setAll(GUI.getInstance().getBusiness().getAvailableDepartments(t1.getId()).getResponse());
            }
        });
    }

    @Override
    public void loaded() {
        choiceBoxUser.getItems().addAll(parent.getAllUsers());
    }

    public void addToDepartmentBtn(ActionEvent actionEvent){
        ConnectionState connectionState = GUI.getInstance().getBusiness().addUserToDepartment(choiceBoxDepartment.getValue().getId(), choiceBoxUser.getValue().getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully added user " + choiceBoxUser.getValue().getName() + " to the department " + choiceBoxDepartment.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }

}
