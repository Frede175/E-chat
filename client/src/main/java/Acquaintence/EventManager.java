package Acquaintence;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

public class EventManager {
    private static EventManager eventManager;
    private HashMap<Class<? extends EventObject>, List<ActionBase>> eventListeners;

    private EventManager() {
        eventListeners = new HashMap<>();
    }

    public <T extends EventObject> void registerListener(Class<T> type, IAction<T> callback) {
        if(!eventListeners.containsKey(type)) {
            eventListeners.put(type, new ArrayList<>());
        }
        ActionBase action = param -> callback.invoke(type.cast(param));
        eventListeners.get(type).add(action);
    }

    public void fireEvent(EventObject eventObject) {
        if(eventListeners.get(eventObject.getClass()) != null) {
            eventListeners.get(eventObject.getClass()).forEach(a -> a.invoke(eventObject));
        }
    }

    public static EventManager getInstance() {
        if (eventManager == null) eventManager =  new EventManager();
        return eventManager;
    }

}
