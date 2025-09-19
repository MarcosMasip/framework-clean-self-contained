package tools.dynamia.app;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal in-memory {@link org.springframework.cache.CacheManager} replacement
 * that creates {@link Ehcache3Cache} instances on demand. All previous
 * configuration knobs related to Ehcache 3 were removed to eliminate the
 * external dependency and simplify offline usage.
 */
public class Ehcache3CacheManager extends AbstractCacheManager {

    private final Map<String, Ehcache3Cache> caches = new LinkedHashMap<>();

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches.values();
    }

    @Override
    protected Cache getMissingCache(String name) {
        Ehcache3Cache cache = new Ehcache3Cache(name);
        caches.put(name, cache);
        return cache;
    }
}
