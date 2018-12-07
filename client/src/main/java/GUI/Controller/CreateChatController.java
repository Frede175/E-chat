package GUI.Controller;


import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Acquaintence.IDepartment;
import Business.Connection.RequestResponse;
import GUI.NotificationUpdater;
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
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getSelectionModel().select(0);
        }

    }


    public void createChat(ActionEvent actionEvent){
        ConnectionState connectionState = GUI.GUI.getInstance().getBusiness().createChat(nameTextField.getText(), choiceBox.getValue().getId());
        Stage stage = (Stage) CreateChatBtnId.getScene().getWindow();
        stage.setScene(GUI.GUI.getInstance().getPrimaryScene());
        String input = "Created chat " + nameTextField.getText();

        NotificationUpdater.getInstance().showNotification(input, connectionState);
    }
}
