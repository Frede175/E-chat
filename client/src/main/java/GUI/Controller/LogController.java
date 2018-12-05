package GUI.Controller;

import Acquaintence.ILogMessage;
import Business.Connection.PermissionEnum;
import Business.Connection.RequestResponse;
import GUI.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class LogController {

    @FXML
    private TableView<Log> table;

    @FXML
    private TableColumn logLevelEnum;

    @FXML
    private TableColumn message;

    @FXML
    private TableColumn timestamp;

    @FXML
    private Button allBtn;

    @FXML
    private Button customBtn;

    ObservableList<Log> logs = FXCollections.observableArrayList();

    private boolean canGetMore = true;
    private boolean isCustom = true;

    public void initialize() {
        logLevelEnum.setCellValueFactory(new PropertyValueFactory<>("logLevel"));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        ArrayList<PermissionEnum> perms = GUI.getInstance().getBusiness().getLoginUser().getAdminPermissions();
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
        Platform.runLater(() -> {
            ScrollBar bar  = (ScrollBar) table.lookup(".scroll-bar:vertical");
            bar.valueProperty().addListener((observableValue, number, t1) -> {
                if (number.doubleValue() != 0 && t1.doubleValue() == bar.getMax() && canGetMore) {
                    int page = table.getItems().size() / 100;
                    RequestResponse<List<? extends ILogMessage>> response;
                    if(isCustom) {
                        response = GUI.getInstance().getBusiness().getCustomLogs(page);
                    } else {
                        response = GUI.getInstance().getBusiness().getAllLogs(page);
                    }
                    List<? extends ILogMessage> newLogs = response.getResponse();
                    newLogs.removeAll(logs);
                    if (!newLogs.isEmpty()) {
                        setItems(newLogs, true);
                        table.scrollTo(logs.get(100 * page));
                    } else {
                        canGetMore = false;
                    }
                }
            });
        });
    }

    public void customLogs(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setItems(GUI.getInstance().getBusiness().getCustomLogs(0).getResponse(), false);
                isCustom = true;
            }
        });
    }

    public void allLogs(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setItems(GUI.getInstance().getBusiness().getAllLogs(0).getResponse(), false);
                isCustom = false;
            }
        });
    }

    private void setItems(List<? extends ILogMessage> l, boolean isLoad) {
        if(!isLoad) {
            logs.clear();
        }
        for (ILogMessage log : l) {
            logs.add(new Log(log.getLogLevelEnum().toString(), log.getMessage(), log.getTimeStamp().toString()));
        }
        SortedList<Log> sortedLogs = new SortedList<>(logs);
        table.setItems(sortedLogs);
    }
}
