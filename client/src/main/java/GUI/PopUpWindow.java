package GUI;

import com.sun.org.apache.bcel.internal.generic.POP;
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

    public static PopUpWindow createUserPopUp() {
        PopUpWindow window = null;
        try {
            window = new PopUpWindow("/fxml/CreateUser.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return window;
    }
}
