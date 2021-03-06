package Starter;

import Acquaintence.IBusinessFacade;
import Acquaintence.IGUI;
import GUI.GUI;
import Business.*;

public class Starter {
    public static void main(String[] args) {

        IGUI gui = new GUI();

        IBusinessFacade business = new BusinessFacade();

        gui.injectBusinessFacade(business);

        gui.startApplication(args);
    }
}
