package Acquaintence;

public interface IBusinessFacade {
    void addDummyData();
    void login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
}
