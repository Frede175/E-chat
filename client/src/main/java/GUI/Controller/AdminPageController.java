package GUI.Controller;

import Acquaintence.IChat;
import Acquaintence.IDepartment;
import Acquaintence.IRole;
import Acquaintence.IUser;
import Business.Connection.PermissionEnum;
import Business.Connection.PermissionType;
import GUI.GUI;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController {

    @FXML
    public TabPane tabs;

    @FXML
    public AnchorPane root;
    private boolean alreadyLog;

    private List<? extends IDepartment> allDepartments;
    private List<? extends IUser> allUsers;
    private List<String> allPermissions;
    private List<? extends IRole> allRoles;
    private List<? extends IChat> allChats;

    public void initialize() {
        ArrayList<PermissionType> types = new ArrayList<>();
        for (PermissionEnum pt : GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions()) {
            if(!types.contains(pt.getType())) {
                types.add(pt.getType());
            }
        }

        //Fix for i3vm
        root.setPrefWidth(GUI.getInstance().getStage().getScene().getWidth());
        root.setPrefHeight(GUI.getInstance().getStage().getScene().getHeight());

        for (PermissionType type : types) {
            Tab tab = new Tab(type.toString());
            tab.setClosable(false);
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox = load(type, vBox);
            ScrollPane sp = new ScrollPane();
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setContent(vBox);
            sp.setFitToHeight(true);
            sp.setFitToWidth(true);

            tab.setContent(sp);
            tabs.getTabs().add(tab);
        }
    }

    public List<? extends IDepartment> getAllDepartments() {
        if (allDepartments == null) {
            allDepartments = GUI.getInstance().getBusiness().getAllDepartments().getResponse();
        }
        return allDepartments;
    }

    public List<? extends IUser> getAllUsers() {
        if (allUsers == null) {
            allUsers = GUI.getInstance().getBusiness().getUsers().getResponse();
        }
        return allUsers;
    }

    public List<? extends IRole> getAllRoles() {
        if (allRoles == null) {
            allRoles = GUI.getInstance().getBusiness().getRoles().getResponse();
        }
        return allRoles;
    }

    public List<? extends IChat> getAllChats() {
        if (allChats == null) {
            allChats = GUI.getInstance().getBusiness().getAllChats().getResponse();
        }
        return allChats;
    }

    public List<String> getAllPermissions() {
        if (allPermissions == null) {
            allPermissions = GUI.getInstance().getBusiness().getAllPermissions().getResponse();
        }
        return allPermissions;
    }

    private void loadFragment(Pane root, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent parent = loader.load();
            root.getChildren().addAll(createSeparator(), parent);
            Controller<AdminPageController> controller = loader.getController();
            controller.setParent(this);
            controller.loaded();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private VBox load(PermissionType type, VBox root) {
        for (PermissionEnum perm : GUI.getInstance().getBusiness().getLoginUser().getUserPermissionsFromType(type)) {
            switch (perm) {
                case CreateDepartment:
                    loadFragment(root, "/fxml/CreateDepartment.fxml");
                    break;
                case AddUserToDepartment:
                    loadFragment(root, "/fxml/AddUserToDepartment.fxml");
                    break;
                case RemoveUserFromDepartment:
                    loadFragment(root, "/fxml/RemoveUserFromDepartment.fxml");
                    break;
                case DeleteDepartment:
                    loadFragment(root, "/fxml/DeleteDepartment.fxml");
                    break;
                case UpdateDepartment:
                    loadFragment(root, "/fxml/UpdateDepartment.fxml");
                    break;
                case CreateChat:
                    loadFragment(root, "/fxml/CreateChat.fxml");
                    break;
                case RemoveChat:
                    loadFragment(root, "/fxml/RemoveChat.fxml");
                    break;
                case AddUserToChat:
                    loadFragment(root, "/fxml/AddUserToChat.fxml");
                    break;
                case RemoveUserFromChat:
                    loadFragment(root, "/fxml/RemoveUserFromChat.fxml");
                    break;
                case CreateUser:
                    loadFragment(root, "/fxml/CreateUser.fxml");
                    break;
                case DeleteUser:
                    loadFragment(root, "/fxml/DeleteUser.fxml");
                    break;
                case AddRoleToUser:
                    loadFragment(root, "/fxml/AddRoleToUser.fxml");
                    break;
                case RemoveRoleFromUser:
                    loadFragment(root, "/fxml/RemoveRoleFromUser.fxml");
                    break;
                case CreateRole:
                    loadFragment(root, "/fxml/CreateRole.fxml");
                    break;
                case AddPermissionToRole:
                    loadFragment(root, "/fxml/AddPermissionsToRole.fxml");
                    break;
                case RemovePermissionFromRole:
                    loadFragment(root, "/fxml/RemovePermissionFromRole.fxml");
                    break;
                case DeleteRole:
                    loadFragment(root, "/fxml/DeleteRole.fxml");
                    break;
                case SeeAllLogs:
                case SeeLogs:
                        if(!alreadyLog) {
                            loadFragment(root, "/fxml/Log.fxml");
                            alreadyLog = true;
                        }
                    break;
            }
        }
        return root;
    }

    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);
        sep.setMinWidth(980);
        sep.setHalignment(HPos.CENTER);
        return sep;
    }

    public void back(ActionEvent actionEvent) {
        GUI.getInstance().loadMainScene();
    }


}
