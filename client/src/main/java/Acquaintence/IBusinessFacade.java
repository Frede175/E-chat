package Acquaintence;

import Business.Connection.RequestResponse;

import java.util.List;

public interface IBusinessFacade {

    ConnectionState login(String username, String password);
    void injectGUINotifier(IGUINotifier guiNotifier);
    RequestResponse<List<? extends IChat>> getChats();

    void sendMessage(String message);


    RequestResponse<List<? extends IMessageIn>> getMessages();

    RequestResponse<List<? extends IUser>> getUsersInChat();
    RequestResponse<List<? extends IDepartment>> getDepartments();

    void setCurrentChat(int chatID);

}
