package Acquaintence;

import Business.Connection.PathEnum;

import java.util.HashMap;
import java.util.List;

public interface IBusinessFacade {

    void addDummyData();
    boolean login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
    <T> T get(PathEnum path, String route, HashMap<String, String> param);
}
