package GUI.Controller;


import Acquaintence.IDepartment;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class CreateChatController {


    public TextField nameTextField;
    public ChoiceBox choiceBox;

    public void initialize(){

        choiceBox.getItems().addAll(1, 2, 3, 4, 5);

    }


    public void createChat(ActionEvent actionEvent){
        GUI.GUI.getInstance().getBusiness().createChat(nameTextField.getText(), 1);

    }


    private void getChoice(ChoiceBox<IDepartment> choiceBox){

    }
}
