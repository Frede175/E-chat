package GUI.Controller;

public abstract class Controller<T> {
    protected T parent;

    public void setParent(T parent) {
        this.parent = parent;
    }

    public void loaded() { }
}
