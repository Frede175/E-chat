package GUI.Controller;

import Acquaintence.IChat;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.awt.event.ActionEvent;

public class DeleteChatController {

    public Button DeleteBtn;
    public ChoiceBox<IChat> cbChat;

    public void initialize(){
        cbChat.getItems().addAll(GUI.GUI.getInstance().getBusiness().getExistingChats());
        if(!cbChat.getItems().isEmpty()){
            cbChat.getSelectionModel().select(0);
        }
    }

    public void delete(){
        GUI.GUI.getInstance().getBusiness().deleteChat(cbChat.getValue().getId());
    }

}
