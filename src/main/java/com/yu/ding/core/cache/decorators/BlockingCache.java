/*
 *    Copyright 2009-2022 the original author or authors.
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
import com.yu.ding.core.cache.CacheException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>Simple blocking decorator
 *
 * <p>Simple and inefficient version of EhCache's BlockingCache decorator.
 * It sets a lock over a cache key when the element is not found in cache.
 * This way, other threads will wait until this element is filled instead of hitting the database.
 *
 * <p>By its nature, this implementation can cause deadlock when used incorrectly.
 *
 * @author mryu
 *
 */
public class BlockingCache<K, V> implements Cache<K, V> {

  private long timeout;
  private final Cache<K, V> delegate;
  private final ConcurrentHashMap<K, CountDownLatch> locks;

  public BlockingCache(Cache<K, V> delegate) {
    this.delegate = delegate;
    this.locks = new ConcurrentHashMap<>();
  }


  @Override
  public void put(K key, V value) {
    try {
      delegate.put(key, value);
    } finally {
      releaseLock(key);
    }
  }

  @Override
  public V get(K key) {
    acquireLock(key);
    V value = delegate.get(key);
    if (value != null) {
      releaseLock(key);
    }
    return value;
  }

  @Override
  public V remove(K key) {
    // despite its name, this method is called only to release locks
    releaseLock(key);
    return null;
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  private void acquireLock(K key) {
    CountDownLatch newLatch = new CountDownLatch(1);
    while (true) {
      CountDownLatch latch = locks.putIfAbsent(key, newLatch);
      if (latch == null) {
        break;
      }
      try {
        if (timeout > 0) {
          boolean acquired = latch.await(timeout, TimeUnit.MILLISECONDS);
          if (!acquired) {
            throw new CacheException(
                "Couldn't get a lock in " + timeout + " for the key " + key + " at the cache " + delegate.getId());
          }
        } else {
          latch.await();
        }
      } catch (InterruptedException e) {
        throw new CacheException("Got interrupted while trying to acquire lock for key " + key, e);
      }
    }
  }

  private void releaseLock(K key) {
    CountDownLatch latch = locks.remove(key);
    if (latch == null) {
      throw new IllegalStateException("Detected an attempt at releasing unacquired lock. This should never happen.");
    }
    latch.countDown();
  }

  public long getTimeout() {
    return timeout;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }
}
