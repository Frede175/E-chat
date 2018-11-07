package Acquaintence;

public interface IAction<T> {
    void invoke(T event);
}
