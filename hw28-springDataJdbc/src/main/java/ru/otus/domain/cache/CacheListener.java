package ru.otus.domain.cache;

public interface CacheListener<K, V> {
    void notify(K key, V value, String action);
}
