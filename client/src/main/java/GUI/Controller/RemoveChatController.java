package GUI.Controller;

import Acquaintence.IChat;
import GUI.GUI;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class RemoveChatController {

    public Button deleteBtn;
    public ChoiceBox<IChat> cbChat;

    public void initialize(){
        cbChat.getItems().addAll(GUI.getInstance().getBusiness().getExistingChats());
        if(!cbChat.getItems().isEmpty()){
            cbChat.getSelectionModel().select(0);
        }
    }

    public void delete(){
        GUI.getInstance().getBusiness().deleteChat(cbChat.getValue().getId());
        Stage stage = (Stage) deleteBtn.getScene().getWindow();
        stage.setScene(GUI.getInstance().getPrimaryScene());
    }

}
