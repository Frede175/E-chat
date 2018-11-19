package GUI.Controller;

import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import GUI.PopUpWindow;

public class LoggedInUserController {

    @FXML
    private ComboBox<PermissionType> adminCB;

    @FXML
    private Label userNameL;

    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());
        for (PermissionEnum pt : GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions()) {
            if(!adminCB.getItems().contains(pt.getType())) {
                adminCB.getItems().add(pt.getType());
            }
        }
        adminCB.valueProperty().addListener(new ChangeListener<PermissionType>() {
            @Override
            public void changed(ObservableValue<? extends PermissionType> observable, PermissionType oldValue, PermissionType newValue) {
                PopUpWindow.createAdminPopUp(newValue);
            }
        });
    }

    public void logout() {
        GUI.getInstance().getBusiness().logout();
    }
}
