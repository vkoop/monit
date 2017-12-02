package de.vkoop.monit;

public interface Filter<T> {
    boolean filter(T t);

    boolean restore(T t);
}
