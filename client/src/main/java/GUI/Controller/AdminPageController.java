package GUI.Controller;

import Acquaintence.ILogMessage;
import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController {

    @FXML
    public TabPane root;
    private boolean alreadyLog;

    public void initialize() {
        ArrayList<PermissionType> types = new ArrayList<>();
        for (PermissionEnum pt : GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions()) {
            if(!types.contains(pt.getType())) {
                types.add(pt.getType());
            }
        }
        for (PermissionType type : types) {
            Tab tab = new Tab(type.toString());
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox = load(type, vBox);
            ScrollPane sp = new ScrollPane();
            sp.setContent(vBox);
            tab.setContent(sp);
            root.getTabs().add(tab);
        }
    }

    public VBox load(PermissionType type, VBox root) {
        //TODO Could be made a function for better clarity
        for (PermissionEnum perm : GUI.getInstance().getBusiness().getLoginUser().getUserPermissionsFromType(type)) {
            switch (perm) {
                case CreateDepartment:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CreateDepartment.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AddUserToDepartment:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AddUserToDepartment.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RemoveUserFromDepartment:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/RemoveUserFromDepartment.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case DeleteDepartment:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/DeleteDepartment.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case UpdateDepartment:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/UpdateDepartment.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CreateChat:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CreateChat.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RemoveChat:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/RemoveChat.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AddUserToChat:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AddUserToChat.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RemoveUserFromChat:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/RemoveUserFromChat.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/DeleteUser.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AddAdditionalRole:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AddRoleToUser.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CreateRole:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CreateRole.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AddPermissionToRole:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AddPermissionsToRole.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case RemovePermissionFromRole:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/RemovePermissionFromRole.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case DeleteRole:
                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/DeleteRole.fxml"));
                        root.getChildren().addAll(createSeparator(), parent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SeeAllLogs:
                case SeeLogs:
                    try {
                        if(!alreadyLog) {
                            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Log.fxml"));
                            root.getChildren().addAll(createSeparator(), parent);
                            alreadyLog = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return root;
    }

    public Separator createSeparator() {
        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);
        sep.setMinWidth(980);
        sep.setHalignment(HPos.CENTER);
        return sep;
    }

    public void back(ActionEvent actionEvent) {
        GUI.getInstance().getStage().setScene(GUI.getInstance().getPrimaryScene());
    }
}
