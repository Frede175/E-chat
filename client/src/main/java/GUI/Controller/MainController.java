package GUI.Controller;



import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ChatListController chatListController;

    @FXML
    private MessageViewController chatBoxController;

    @FXML
    public void initialize() {
        chatListController.getChats();
        chatBoxController.getMessages();
    }

}
