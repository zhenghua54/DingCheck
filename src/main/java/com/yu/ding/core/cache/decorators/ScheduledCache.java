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

import java.util.concurrent.TimeUnit;

/**
 * @author mryu
 */
public class ScheduledCache<K, V> implements Cache<K, V> {

  private final Cache<K, V> delegate;
  protected long clearInterval;
  protected long lastClear;

  public ScheduledCache(Cache<K, V> delegate) {
    this.delegate = delegate;
    this.clearInterval = TimeUnit.HOURS.toMillis(1);
    this.lastClear = System.currentTimeMillis();
  }

  public void setClearInterval(long clearInterval) {
    this.clearInterval = clearInterval;
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public void put(K key, V object) {
    clearWhenStale();
    delegate.put(key, object);
  }

  @Override
  public V get(K key) {
    return clearWhenStale() ? null : delegate.get(key);
  }

  @Override
  public V remove(K key) {
    clearWhenStale();
    return delegate.remove(key);
  }

  @Override
  public void clear() {
    lastClear = System.currentTimeMillis();
    delegate.clear();
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return delegate.equals(obj);
  }

  private boolean clearWhenStale() {
    if (System.currentTimeMillis() - lastClear > clearInterval) {
      clear();
      return true;
    }
    return false;
  }

}
