package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import GUI.GUI;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import GUI.NotificationUpdater;

public class DeleteChatController extends Controller<AdminPageController> {

    public Button deleteBtn;
    public ChoiceBox<IChat> cbChat;

    @Override
    public void loaded() {
        cbChat.getItems().addAll(parent.getAllChats());
        if(!cbChat.getItems().isEmpty()){
            cbChat.getSelectionModel().select(0);
        }
    }

    public void delete(){
        ConnectionState connectionState = GUI.getInstance().getBusiness().deleteChat(cbChat.getValue().getId());

        GUI.getInstance().loadMainScene();
        String input = "Succesfully deleted the chat " + cbChat.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }

}
