package GUI.Controller;

import javafx.beans.property.SimpleStringProperty;

public class Log {

    private final SimpleStringProperty logLevel;
    private final SimpleStringProperty message;
    private final SimpleStringProperty timestamp;

    public Log(String logLevel, String message, String timestamp) {
        this.logLevel = new SimpleStringProperty(logLevel);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(timestamp);
    }

    public String getLogLevel() {
        return logLevel.get();
    }

    public void setLogLevel(String logLevel) {
        this.logLevel.set(logLevel);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }
}