package GUI;

import Acquaintence.IBusinessFacade;
import Acquaintence.IGUI;
import Acquaintence.IMessageReceiver;
import Business.Connection.PathEnum;
import Business.Models.Chat;
import Business.Models.Department;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
        gui.stage = stage;
        stage.setScene(scene);
        stage.setTitle("E-Chat");
        //stage.getIcons().add(new Image("Images/icon.png"));
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

    @Override
    public IMessageReceiver getMessageReceiver() {
        return new MessageReceiver();
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
