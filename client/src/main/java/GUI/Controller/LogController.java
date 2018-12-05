package GUI.Controller;

import Acquaintence.ILogMessage;
import Business.Connection.PermissionEnum;
import GUI.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LogController {

    @FXML
    private TableView<Log> table;

    @FXML
    private TableColumn logLevel;

    @FXML
    private TableColumn message;

    @FXML
    private TableColumn timestamp;

    @FXML
    private Button allBtn;

    @FXML
    private Button customBtn;

    ObservableList<Log> logs = FXCollections.observableArrayList();g

    public void initialize() {
        logLevel.setCellValueFactory(new PropertyValueFactory<>("logLevel"));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        Set<PermissionEnum> perms = GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions();
        for(PermissionEnum perm : perms) {
            if(perm == PermissionEnum.SeeAllLogs) {
                allBtn.setVisible(true);
                table.setVisible(true);
            }
            if(perm == PermissionEnum.SeeLogs) {
                customBtn.setVisible(true);
                table.setVisible(true);
            }
        }
    }

    public void customLogs(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setItems(GUI.getInstance().getBusiness().getCustomLogs().getResponse());
            }
        });
    }

    public void allLogs(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setItems(GUI.getInstance().getBusiness().getAllLogs().getResponse());
            }
        });
    }

    private void setItems(List<? extends ILogMessage> l) {
        logs.clear();
        for (ILogMessage log : l) {
            logs.add(new Log(log.getLogLevel().toString(), log.getMessage(), log.getTimeStamp().toString()));
        }
        SortedList<Log> sortedLogs = new SortedList<>(logs);
        table.setItems(sortedLogs);
    }
}
