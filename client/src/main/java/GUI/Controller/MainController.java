package GUI.Controller;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import GUI.GUI;
import javafx.fxml.FXML;
import GUI.NotificationUpdater;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.spreadsheet.Grid;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    private MessageViewController messageViewController;

    @FXML
    private GridPane root;

    @FXML
    public void initialize() {
        //Fix for i3vm
        root.setPrefWidth(GUI.getInstance().getStage().getScene().getWidth());
        root.setPrefHeight(GUI.getInstance().getStage().getScene().getHeight());

        EventManager.getInstance().registerListener(MessageEvent.class, this::showNotification);
        messageViewController.getMessages();
    }

    public void showNotification(MessageEvent event) {
        NotificationUpdater.getInstance().showNotification(event);
    }
}
