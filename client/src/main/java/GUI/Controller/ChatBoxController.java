package GUI.Controller;


import Acquaintence.IMessageIn;
import Business.Connection.RequestResponse;
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
        RequestResponse<List<? extends IMessageIn>> response  = GUI.getInstance().getBusiness().getMessages();

        System.out.println(response.toString());

        if(response == null){
            System.out.println("No messages cunt");
        }else{
            System.out.println("I LIVESSS IT LIVESSS");
            ObservableList<String> messages = FXCollections.observableArrayList ();
            for (IMessageIn message : response.getResponse()) {
                messages.add(message.getTimeStamp() + " " + message.getUser().getName() + "     " + message.getContent());
            }

            chatBox.setItems(messages);
        }


    }
}