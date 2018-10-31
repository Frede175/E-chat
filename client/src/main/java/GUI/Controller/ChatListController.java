package GUI.Controller;

import Business.Connection.PathEnum;
import Business.Models.Chat;
import Business.Models.Department;
import com.ibm.icu.text.ArabicShaping;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ChatListController {

    private Department department = new Department(1, "jeff");

    @FXML
    public ListView<String> chatList;

    public void getData() {

        String token = GUI.GUI.getInstance().getBusiness().login("Admin1", "Password123*");

        ArrayList<String> stringList = new ArrayList<>();
        //chatList = new ListView<>();
        List<Chat> chats = GUI.GUI.getInstance().getBusiness().get(PathEnum.GetChats, "d8d65767-ca69-4abb-974e-a21883096b4e", department.toMap(),  token);
        if(chats != null) {
            System.out.println("Not null");
            for (Chat chat : chats) {
                stringList.add(chat.getName());
            }
        }
        ObservableList<String> names = FXCollections.observableArrayList(stringList);
        chatList.setItems(names);

    }

}
