package GUI;

import Acquaintence.IBusinessFacade;
import Acquaintence.IGUINotifier;
import Acquaintence.IMessageIn;

public class GUINotifier implements IGUINotifier {



    @Override
    public void recieve(IMessageIn message) {
        //businessFacade.getCurrentChat().addMessage(message);
        // TODO Call update on GUI
    }
}
