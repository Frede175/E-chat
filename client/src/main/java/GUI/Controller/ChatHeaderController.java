package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ChatHeaderController {

    @FXML
    private Label chatNameL;

    @FXML
    private Button cHButton;


    public void initialize(){

        Label l = new Label("Lars du faktisk ikke så sej..");
        VBox b = new VBox(l);
        b.setStyle("-fx-background-color: #F399D7");

        PopOver pop = new PopOver(b);
        pop.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);

        chatNameL.setOnMouseEntered(MouseEvent -> { pop.show(chatNameL);});
        chatNameL.setOnMouseExited(MouseEvent -> {pop.hide();});
    }

    public void usersInChatBtn(ActionEvent actionEvent) throws IOException {

        PopOver popover = new PopOver();

        chatNameL.setText(("Current chat: TODO"));

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/UsersInChat.fxml"));

        popover.setAutoFix(true);
        popover.setAutoHide(true);
        popover.setHideOnEscape(true);
        popover.setDetachable(false);

        popover.setTitle("FUCK THIS SHIT FUCK CUNT");
        popover.setContentNode(root);

        //Scene scene = new Scene(root);

        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.show(cHButton);
    }
}

