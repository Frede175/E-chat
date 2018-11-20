package GUI.Controller;


import Acquaintence.IDepartment;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateChatController {


    public TextField nameTextField;
    public ChoiceBox<IDepartment> choiceBox;
    public Button CreateChatBtnId;

    public void initialize(){

        choiceBox.getItems().addAll(GUI.GUI.getInstance().getBusiness().getAllDepartments().getResponse());

    }


    public void createChat(ActionEvent actionEvent){
        GUI.GUI.getInstance().getBusiness().createChat(nameTextField.getText(), choiceBox.getValue().getId());

        Stage stage = (Stage) CreateChatBtnId.getScene().getWindow();
        stage.close();

    }
}
