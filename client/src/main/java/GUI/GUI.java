package GUI;

import Acquaintence.IBusinessFacade;
import Acquaintence.IGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application implements IGUI {
    /**
     * An instance of the GUI class.
     */
    private static GUI gui;

    private Stage stage;

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
        stage.setResizable(false);
        gui.stage = stage;
        stage.setScene(scene);
        stage.setTitle("E-Chat");
        stage.getIcons().add(new Image("img/E-chat.png"));
        stage.show();
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
     * Gets the business facade
     * @return the business facade
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
}
