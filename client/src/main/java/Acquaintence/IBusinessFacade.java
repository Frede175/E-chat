package Acquaintence;

import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;

import java.util.HashMap;
import java.util.List;

public interface IBusinessFacade {
    ConnectionState login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
    RequestResponse<List<IChat>> getChats();
    void sendMessage(String message);
    <T> RequestResponse<T> getUsersInChat();
    RequestResponse<List<IDepartment>> getDepartments();
}
