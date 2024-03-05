package com.yu.ding.core.cache;

/**
 *
 * @author MrYU
 */
public interface Cache<K, V> {
    /**
     * put cache value
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * get cache value
     * @param key
     * @return
     */
    V get(K key);

    /**
     * remove the object
     * @param key
     */
    V remove(K key);

    /**
     * clear the cache
     */
    void clear();

    /**
     * get the tag of cache
     * @return
     */
    String getId();
}
