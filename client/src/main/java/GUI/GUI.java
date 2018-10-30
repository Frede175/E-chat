package GUI;

import Acquaintence.IBusinessFacade;
import Acquaintence.IGUI;
import Acquaintence.IMessageReceiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application implements IGUI {
    /**
     * An instance of the GUI class.
     */
    private static GUI gui;

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
        /*Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Main.fxml"));
        Scene scene = new Scene(root);

        stage.setMinHeight(720.0);
        stage.setMinWidth(1280.0);
        stage.setScene(scene);
        stage.setTitle("E-Chat");
        stage.getIcons().add(new Image("Images/icon.png"));
        stage.show();*/
        business.addDummyData();
        business.login("username", "password");
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
}
