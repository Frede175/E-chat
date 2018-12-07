package GUI.Controller;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import javafx.fxml.FXML;
import GUI.NotificationUpdater;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    private MessageViewController messageViewController;


    @FXML
    public void initialize() {
        EventManager.getInstance().registerListener(MessageEvent.class, this::showNotification);
        messageViewController.getMessages();
    }

    public void showNotification(MessageEvent event) {
        NotificationUpdater.getInstance().showNotification(event);
    }
}
