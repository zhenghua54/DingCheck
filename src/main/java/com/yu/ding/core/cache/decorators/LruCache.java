/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.yu.ding.core.cache.decorators;

import com.yu.ding.core.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lru (least recently used) cache decorator.
 *
 * @author mryu
 */
public class LruCache<K, V> implements Cache<K, V> {

  private final Cache<K, V> delegate;
  private Map<K, K> keyMap;
  private K eldestKey;

  public LruCache(Cache<K, V> delegate) {
    this.delegate = delegate;
    setSize(1024);
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  public void setSize(final int size) {
    keyMap = new LinkedHashMap<K, K>(size, .75F, true) {
      private static final long serialVersionUID = 4267176411845948333L;

      @Override
      protected boolean removeEldestEntry(Map.Entry<K, K> eldest) {
        boolean tooBig = size() > size;
        if (tooBig) {
          eldestKey = eldest.getKey();
        }
        return tooBig;
      }
    };
  }

  @Override
  public void put(K key, V value) {
    delegate.put(key, value);
    cycleKeyList(key);
  }

  @Override
  public V get(K key) {
    keyMap.get(key); // touch
    return delegate.get(key);
  }

  @Override
  public V remove(K key) {
    //the keymap size should equal delegate size
    keyMap.remove(key);
    return delegate.remove(key);
  }

  @Override
  public void clear() {
    delegate.clear();
    keyMap.clear();
  }

  private void cycleKeyList(K key) {
    keyMap.put(key, key);
    if (eldestKey != null) {
      delegate.remove(eldestKey);
      eldestKey = null;
    }
  }

}
