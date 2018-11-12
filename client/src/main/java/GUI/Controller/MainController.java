package GUI.Controller;



import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    private ChatBoxController chatBoxController;

    @FXML
    public void initialize() {
        chatListController.getChats();
        chatBoxController.getMessages();
    }

}
