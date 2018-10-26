package Acquaintence;

public interface IGUI {
    void injectBusinessFacade(IBusinessFacade businessFacade);
    void startApplication(String[] args);

    IMessageReceiver getMessageReceiver();
}
