package GUI;

import Business.Connection.PermissionType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class PopUpWindow {

    private Stage stage;

    private PopUpWindow(String url) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        System.out.println(stage.getOwner());
        stage.initOwner(stage.getOwner());
        stage.showAndWait();
    }

    public static PopUpWindow createAdminPopUp(PermissionType type) {
        PopUpWindow window = null;
        try {
            switch (type) {
                case CHAT:
                    //window = new PopUpWindow("/fxml/CreateUser.fxml");
                    System.out.println("Open Chat admin");
                    break;
                case DEPARTMENT:
                    //window = new PopUpWindow("/fxml/CreateUser.fxml");
                    System.out.println("Open Department admin");
                    break;
                case USER:
                    //window = new PopUpWindow("/fxml/CreateUser.fxml");
                    System.out.println("Open User admin");
                    break;
                case ROLE:
                    System.out.println("Open Role admin");
                    window = new PopUpWindow("/fxml/CreateUser.fxml");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return window;
    }
}
