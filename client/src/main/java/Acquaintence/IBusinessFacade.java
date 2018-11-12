package Acquaintence;

import Business.Connection.RequestResponse;

import java.util.List;

public interface IBusinessFacade {

    ConnectionState login(String username, String password);
    RequestResponse<List<? extends IChat>> getChats();

    void sendMessage(String message);


    RequestResponse<List<? extends IMessageIn>> getMessages();

    RequestResponse<List<? extends IUser>> getUsersInChat();
    RequestResponse<List<? extends IDepartment>> getDepartments();

    IChat getCurrentChat();
    void setCurrentChat(int chatID);

    void createUser(String username, String password);

}
