package GUI.Controller;

import Acquaintence.IUser;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.awt.event.ActionEvent;

public class AddRoleToUserController {

    @FXML
    ComboBox<String> selectUser;

    @FXML
    ComboBox<String> selectRole;

    @FXML
    Button addRoleToUser;

    private String selectedUser;
    private String selectedRole;

    public void initialize() {
        for (IUser user : GUI.getInstance().getBusiness().getUsers().getResponse()) {
            if(!selectUser.getItems().contains(user.getName())) {
                selectUser.getItems().add(user.getName());
            }
        }
        if(!selectUser.getItems().isEmpty()) {
            selectUser.getSelectionModel().select(0);
            selectedUser = selectUser.getItems().get(0);
        }
        selectUser.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedUser = t1;
            }
        });

        for (IUser user : GUI.getInstance().getBusiness().getUsers().getResponse()) {
            if(!selectRole.getItems().contains(user.getName())) {
                selectRole.getItems().add(user.getName());
            }
        }
        if(!selectRole.getItems().isEmpty()) {
            selectRole.getSelectionModel().select(0);
            selectedRole = selectRole.getItems().get(0);
        }
        selectRole.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedRole = t1;
            }
        });
    }

    public void addSelected(javafx.event.ActionEvent actionEvent) {
        GUI.getInstance().getBusiness().addRoleToUser(selectedUser, selectedRole);
    }
}
