package GUI.Controller;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    public void initialize() {
        chatListController.getData();
    }
}
