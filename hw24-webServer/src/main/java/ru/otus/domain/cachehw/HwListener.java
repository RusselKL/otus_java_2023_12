package ru.otus.domain.cachehw;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
