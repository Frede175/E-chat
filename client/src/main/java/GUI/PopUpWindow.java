package GUI;

import Business.Connection.PermissionType;
import GUI.Controller.AdminPageController;
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

    private PopUpWindow(String url, PermissionType type) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent p = loader.load(getClass().getResource("/fxml/AdminPage.fxml").openStream());
        AdminPageController controller = loader.getController();
        controller.load(type);
        Scene scene = new Scene(p);
        stage = new Stage();
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.initOwner(stage.getOwner());
        stage.showAndWait();
    }

    public static PopUpWindow createAdminPopUp(PermissionType type) {
        PopUpWindow window = null;
        try {
            window = new PopUpWindow("/fxml/AdminPage.fxml", type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return window;
    }
}
