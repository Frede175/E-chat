package Acquaintence;

import Business.Connection.PathEnum;
import Business.Connection.RequestResponse;

import java.util.HashMap;
import java.util.List;

public interface IBusinessFacade {

    ConnectionState login(String username, String password);
    void injectMessageReceiver(IMessageReceiver messageReceiver);
    RequestResponse<List<? extends IChat>> getChats();

    void sendMessage(String message);


    RequestResponse<List<? extends IMessageIn>> getMessages();

    RequestResponse<List<? extends IUser>> getUsersInChat();
    RequestResponse<List<? extends IDepartment>> getDepartments();

}
