package GUI.Controller;

import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminPageController {

    @FXML
    public VBox root;

    @FXML
    public Label title;

    public void load(PermissionType type) {
        title.setText(type.toString());
        for (PermissionEnum perm : GUI.GUI.getInstance().getBusiness().getLoginUser().getUserPermissionsFromType(type)) {
            switch (perm) {
                case CreateDepartment:
                    break;
                case AddUserToDepartment:
                    break;
                case RemoveUserFromDepartment:
                    break;
                case DeleteDepartment:
                    break;
                case UpdateDepartment:
                    break;
                case CreateChat:
                    break;
                case LeaveChat:
                    break;
                case AddUserToChat:
                    break;
                case RemoveUserFromChat:
                    break;
                case GetAllChats:
                    break;
                case CreateUser:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CreateUser.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case DeleteUser:
                    break;
                case AddAdditionalRole:
                    break;
                case CreateUserRole:
                    break;
                case AddPermissionToRole:
                    break;
                case RemovePermissionFromRole:
                    break;
                case DeleteRole:
                    break;
            }
        }
    }

    public Separator createSeparator() {
        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);
        sep.setMinWidth(1000);
        sep.setHalignment(HPos.CENTER);
        return sep;
    }

}
