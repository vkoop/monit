package de.vkoop.monit.filter;

public interface Filter<T> {
    boolean canPass(T item);
}
