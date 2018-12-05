package GUI;

import Acquaintence.ConnectionState;
import Business.Connection.ConnectionType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;

public class NotificationUpdater {


    private static NotificationUpdater instance = new NotificationUpdater();

    public static NotificationUpdater getInstance() {
        if(!(instance == null)) {
            return instance;
        } else
            return instance = new NotificationUpdater();
    }

    public void showNotification(String input, ConnectionState connectionState) {
        Platform.runLater(() -> {
                ImageView logo = new ImageView(new Image("img/E-chat.png"));
                logo.setFitHeight(40.0);
                logo.setFitWidth(40.0);
                Notifications.create()
                        .title(connectionState.getLevel())
                        .text(returnMssage(input,connectionState))
                        .graphic(logo)
                        .show();

        });
    }

    private String returnMssage(String input, ConnectionState connectionState) {
        switch (connectionState) {
            case SUCCESS:
                return input;
            case WRONG_LOGIN:
                return "Login information invalid";
            case NO_BASIC_PERMISSIONS:
                return "You do not have the rights to this";
            case NO_CONNECTION:
                return "There was no connection";
            case ERROR:
                return "Encountered an unkown error";
            case UNAUTHORIZED:
                return "You were not authorized";
            case SERVERERROR:
                return "Internal error on the server side";
            case NOT_FOUND:
                return "Not found";
            case BAD_REQUEST:
                return "There was a problem with the input";
            default:
                return "There was an error";
        }

    }
}
