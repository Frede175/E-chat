package GUI.Controller;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    private MessageViewController messageViewController;

    @FXML
    public void initialize() {
        messageViewController.getMessages();

    }

}
