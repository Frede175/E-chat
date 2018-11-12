package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ChatHeaderController {

    @FXML
    private Label chatNameL;

    @FXML
    private Button cHButton;

    public void usersInChat(ActionEvent actionEvent) {
        chatNameL.setText("Lars er mega nice og sej");




    }

    public void usersInChatBtn(ActionEvent actionEvent) throws IOException {

        // Place "box" into the PopOver(); to get back to working mode.
        PopOver popover = new PopOver();

        chatNameL.setText(("Current chat: TODO"));

        // Original Parent instead of BorderPane
        AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/UsersInChat.fxml"));


        /*VBox box = new VBox(root);
        box.setPrefHeight(200);
        box.setPrefWidth(200);
        */

        popover.setContentNode(new Label("Test"));
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



   /* @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                //controller.hidePopOver();

            }
        });*/
}

