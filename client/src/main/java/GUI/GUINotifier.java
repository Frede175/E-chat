package GUI;

import Acquaintence.IGUINotifier;
import Business.Models.MessageIn;

public class GUINotifier implements IGUINotifier {

    @Override
    public void recieve(MessageIn message) {
        System.out.println("Besked modtaget");
    }
}
