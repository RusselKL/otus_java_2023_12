package ru.otus.domain.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    private static final String PUT = "put";
    private static final String REMOVE = "remove";
    private static final String GET = "get";

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, PUT);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notifyListeners(key, value, REMOVE);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notifyListeners(key, value, GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }

    public int size() {
        return cache.size();
    }

    public void cacheInfo() {
        log.info("Cache info: {}", cache.values());
    }
}
