package GUI.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeaveChatController {

    public Button leaveBtn;
    public Label lblChat;

    public int currentChat;



    public void initialize(){

        lblChat.setText(GUI.GUI.getInstance().getBusiness().getCurrentChat().getName());
        currentChat = GUI.GUI.getInstance().getBusiness().getCurrentChat().getId();

    }

    public void leaveChat(){
        GUI.GUI.getInstance().getBusiness().leaveChat(currentChat);
    }

}

