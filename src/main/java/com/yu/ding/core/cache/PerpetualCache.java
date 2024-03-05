package com.yu.ding.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author MrYu
 */
public class PerpetualCache<K, V> implements Cache<K, V> {
    /**
     * default uuid
     */
    private String id;
    private Map<K, V> caches;

    public PerpetualCache() {
        caches = new HashMap<>();
        generateId();
    }

    public PerpetualCache(Map<K, V> caches) {
        this.caches = caches;
        generateId();
    }

    public PerpetualCache(String id, Map<K, V> caches) {
        this.id = id;
        this.caches = caches;
    }

    @Override
    public void put(K key, V value) {
        caches.put(key, value);
    }

    @Override
    public V get(K key) {
        return caches.get(key);
    }

    @Override
    public V remove(K key) {
        return caches.remove(key);
    }

    @Override
    public void clear() {
        caches.clear();
    }

    @Override
    public String getId() {
        return this.id;
    }
    private void generateId(){
        id = UUID.randomUUID().toString();
    }
}
