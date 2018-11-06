package GUI.Controller;


import Business.Connection.RequestResponse;
import Business.Models.MessageIn;
import GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;


public class ChatBoxController{

    @FXML
    public ListView chatBox;

    // Takes the content from MessagesIn and shows it in the ListVeiw in the GUI TODO it should work.. but not quite yet
    public void getData(){
        RequestResponse<List<MessageIn>> response  = GUI.getInstance().getBusiness().getMessages();

        ObservableList<String> messages = FXCollections.observableArrayList ();
        for (MessageIn message : response.getResponse()) {
            messages.add(message.getTimeStamp() + " " + message.getUser().getName() + "     " + message.getContent());
        }

        chatBox.setItems(messages);

    }
}