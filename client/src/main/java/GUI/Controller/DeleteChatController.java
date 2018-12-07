package GUI.Controller;

import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import GUI.GUI;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import GUI.NotificationUpdater;

public class DeleteChatController {

    public Button deleteBtn;
    public ChoiceBox<IChat> cbChat;

    public void initialize(){
        cbChat.getItems().addAll(GUI.getInstance().getBusiness().getExistingChats());
        if(!cbChat.getItems().isEmpty()){
            cbChat.getSelectionModel().select(0);
        }
    }

    public void delete(){
        ConnectionState connectionState = GUI.getInstance().getBusiness().deleteChat(cbChat.getValue().getId());
        Stage stage = (Stage) deleteBtn.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
        String input = "Succesfully deleted the chat " + cbChat.getValue().getName();
        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }

}
