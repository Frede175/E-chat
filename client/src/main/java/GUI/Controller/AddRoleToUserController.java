package GUI.Controller;

import Acquaintence.IRole;
import Acquaintence.IUser;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.List;

public class AddRoleToUserController {

    @FXML
    ComboBox<IUser> selectUser;

    @FXML
    ComboBox<IRole> selectRole;

    @FXML
    Button addRoleToUser;

    private String selectedUser;
    private String selectedRole;

    public void initialize() {
        //TODO Make labels or something for clarity
        for (IUser user : GUI.getInstance().getBusiness().getUsers().getResponse()) {
            selectUser.getItems().add(user);
        }
        selectUser.valueProperty().addListener(new ChangeListener<IUser>() {
            @Override
            public void changed(ObservableValue<? extends IUser> observableValue, IUser iUser, IUser t1) {
                selectedUser = t1.getId();
            }
        });

        for (IRole role : GUI.getInstance().getBusiness().getRoles().getResponse()) {
            selectRole.getItems().add(role);
        }

        selectRole.valueProperty().addListener(new ChangeListener<IRole>() {
            @Override
            public void changed(ObservableValue<? extends IRole> observableValue, IRole iRole, IRole t1) {
                selectedRole = t1.getName();
            }
        });
    }

    public void addSelected(javafx.event.ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().addRoleToUser(selectedUser, selectedRole);
        Stage stage = (Stage) addRoleToUser.getScene().getWindow();
        stage.close();
    }
}
