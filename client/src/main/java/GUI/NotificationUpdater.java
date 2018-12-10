package GUI;

import Acquaintence.ConnectionState;
import Acquaintence.Event.MessageEvent;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
                        .darkStyle()
                        .show();

        });
    }

    public void showNotification(MessageEvent event) {
        Platform.runLater(() -> {
            if(GUI.getInstance().getBusiness().getLoginUser() != null && !GUI.getInstance().getBusiness().getLoginUser().getSub().equals(event.getMessageIn().getUser().getId())) {
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
