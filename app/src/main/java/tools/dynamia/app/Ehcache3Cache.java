package tools.dynamia.app;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight in-memory cache replacement for the former Ehcache3 integration.
 * <p>
 * This class intentionally removes the external Ehcache dependency to simplify
 * offline builds. It keeps only the minimal behavior required by Spring's
 * {@link org.springframework.cache.Cache} abstraction using a thread-safe
 * {@link ConcurrentHashMap} as backing store.
 */
public class Ehcache3Cache extends AbstractValueAdaptingCache {

    private final Map<Object, Object> store = new ConcurrentHashMap<>();
    private final String name;

    public Ehcache3Cache(String name) {
        super(true);
        this.name = name;
    }

    public Ehcache3Cache(boolean allowNullValues, String name) {
        super(allowNullValues);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return store;
    }

    @Override
    protected Object lookup(Object key) {
        return store.get(key);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = store.get(key);
        if (value == null) {
            try {
                value = toStoreValue(valueLoader.call());
                store.put(key, value);
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
        }
        return (T) fromStoreValue(value);
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        store.put(key, toStoreValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object existing = store.putIfAbsent(key, toStoreValue(value));
        return toValueWrapper(existing);
    }

    @Override
    public void evict(Object key) {
        store.remove(key);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
