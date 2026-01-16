package util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Minimal in-memory cache with hit/miss tracking.
 */
public class SimpleCache<K, V> {
    private final ConcurrentHashMap<K, V> store = new ConcurrentHashMap<>();
    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();
    private final AtomicLong putCount = new AtomicLong();

    public V get(K key) {
        V value = store.get(key);
        if (value != null) {
            hitCount.incrementAndGet();
        } else {
            missCount.incrementAndGet();
        }
        return value;
    }

    public void put(K key, V value) {
        store.put(key, value);
        putCount.incrementAndGet();
    }

    public void invalidate(K key) {
        store.remove(key);
    }

    public void clear() {
        store.clear();
    }

    public long size() {
        return store.size();
    }

    public Stats stats() {
        long hits = hitCount.get();
        long misses = missCount.get();
        long total = hits + misses;
        double hitRate = total == 0 ? 0.0 : (double) hits / total;
        return new Stats(hits, misses, putCount.get(), hitRate);
    }

    public static class Stats {
        public final long hits;
        public final long misses;
        public final long puts;
        public final double hitRate;

        public Stats(long hits, long misses, long puts, double hitRate) {
            this.hits = hits;
            this.misses = misses;
            this.puts = puts;
            this.hitRate = hitRate;
        }
    }
}
