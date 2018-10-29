package Acquaintence;

public interface IBusinessFacade {
    void login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
}
