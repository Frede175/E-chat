package Acquaintence;

public interface IBusinessFacade {
    void addDummyData();
    boolean login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
}
