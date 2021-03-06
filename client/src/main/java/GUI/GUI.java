package GUI;

import Acquaintence.EventManager;
import Acquaintence.IBusinessFacade;
import Acquaintence.IGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application implements IGUI {
    /**
     * An instance of the GUI class.
     */
    private static GUI gui;

    private Stage stage;

    private Scene mainScene;


    /**
     * An instance of the IBusinessFacade.
     */
    private IBusinessFacade business;

    @Override
    public void injectBusinessFacade(IBusinessFacade businessFacade) {
        this.business = businessFacade;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(true);
        gui.stage = stage;
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.setTitle("E-Chat");
        stage.getIcons().add(new Image("img/E-chat.png"));
        stage.show();

    }

    @Override
    public void stop(){
        GUI.getInstance().getBusiness().disconnectHub();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Starts and launches the application.
     *
     * @param args
     */
    @Override
    public void startApplication(String[] args) {
        gui = this;
        launch(args);
    }

    /**
     * Gets the Business facade
     * @return the Business facade
     */
    public IBusinessFacade getBusiness() {
        return business;
    }

    /**
     * Get the ui instance
     * @return the gui object
     */
    public static GUI getInstance() {
        return gui;
    }

    public Stage getStage() {
        return stage;
    }



    public void loadMainScene() {
        if (mainScene == null) {
            loadScene("/fxml/Main.fxml");
            mainScene = stage.getScene();
        } else {
            stage.setScene(mainScene);
        }
    }

    public void loadMainSceneFromLogin() {
        mainScene = null;
        loadMainScene();
    }

    public void loadScene(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
