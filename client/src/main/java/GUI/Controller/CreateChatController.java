package GUI.Controller;


import Acquaintence.ConnectionState;
import Acquaintence.IChat;
import Acquaintence.IDepartment;
import Business.Connection.RequestResponse;
import GUI.GUI;
import GUI.NotificationUpdater;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateChatController extends Controller<AdminPageController> {


    public TextField nameTextField;
    public ChoiceBox<IDepartment> choiceBox;
    public Button CreateChatBtnId;


    public void initialize(){
    }

    @Override
    public void loaded() {
        choiceBox.getItems().addAll(parent.getAllDepartments());
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getSelectionModel().select(0);
        }
    }


    public void createChat(ActionEvent actionEvent){

        ConnectionState connectionState = GUI.getInstance().getBusiness().createChat(nameTextField.getText(), choiceBox.getValue().getId());

        GUI.getInstance().loadMainScene();

        String input = "Created chat " + nameTextField.getText();

        NotificationUpdater.getInstance().showNotification(input, connectionState);


    }


}
