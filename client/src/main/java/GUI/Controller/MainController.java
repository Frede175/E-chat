package GUI.Controller;

import Acquaintence.Event.MessageEvent;
import Acquaintence.EventManager;
import GUI.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

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
        Platform.runLater(() -> {
            if(GUI.getInstance().getBusiness().getLoginUser() != null && GUI.getInstance().getBusiness().getLoginUser().getSub().equals(event.getMessageIn().getUser().getId())) {
                ImageView logo = new ImageView(new Image("img/E-chat.png"));
                logo.setFitHeight(40.0);
                logo.setFitWidth(40.0);
                Notifications.create()
                        .title(event.getMessageIn().getUser().getName())
                        .text(event.getMessageIn().getContent())
                        .graphic(logo)
                        .show();
            }
        });
    }

}
