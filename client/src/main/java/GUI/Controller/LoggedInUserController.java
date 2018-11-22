package GUI.Controller;

import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import GUI.PopUpWindow;

import java.util.ArrayList;

public class LoggedInUserController {


    @FXML
    private Label userNameL;

    @FXML
    private MenuButton adminMB;

    public void initialize() {
        userNameL.setText(GUI.getInstance().getBusiness().getLoginUser().getName());
        ArrayList<PermissionType> types = new ArrayList<>();
        for (PermissionEnum pt : GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions()) {
            if(!types.contains(pt.getType())) {
                types.add(pt.getType());
            }

        }
        for (PermissionType type : types) {
            MenuItem mi = new MenuItem(type.toString());
            mi.setOnAction(event -> {
                PopUpWindow.createAdminPopUp(type);
            });
            adminMB.getItems().add(mi);
        }
    }

    public void logout() {
        GUI.getInstance().getBusiness().logout();
    }
}
