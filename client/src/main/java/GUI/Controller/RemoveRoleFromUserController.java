package GUI.Controller;

import Acquaintence.IRole;
import Acquaintence.IUser;
import GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RemoveRoleFromUserController {

    @FXML
    ComboBox<IUser> selectUser;

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button removeRoleFromUser;


    private List<IRole> roleList;


    public void initialize(){
        roleList = new ArrayList<>();

        roleList.addAll(GUI.getInstance().getBusiness().getRoles().getResponse());

        selectUser.getItems().addAll(GUI.getInstance().getBusiness().getUsers().getResponse());

        selectUser.valueProperty().addListener((observableValue, iUser, t1) -> {
            List<IRole> newRoles = new ArrayList<>(roleList);
            newRoles.removeAll(GUI.getInstance().getBusiness().getAvailableRoles(t1.getId()).getResponse());
            selectRole.getItems().setAll(newRoles);
        });


    }

    public void removeSelected(ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().removeRoleFromUser(selectUser.getValue().getId(), selectRole.getValue().getId());

        GUI.getInstance().loadMainScene();
    }


}