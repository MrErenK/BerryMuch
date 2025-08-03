package com.mrerenk.berrymuch.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.component.ComponentMap;

public class ComponentCacheManager {

    // Cache for modified components to avoid repeated modifications
    private static final Map<String, ComponentMap> modifiedComponentsCache =
        new ConcurrentHashMap<>();

    public static void cacheComponent(String itemId, ComponentMap components) {
        modifiedComponentsCache.put(itemId, components);
    }

    public static ComponentMap getCachedComponent(String itemId) {
        return modifiedComponentsCache.get(itemId);
    }

    public static boolean isCached(String itemId) {
        return modifiedComponentsCache.containsKey(itemId);
    }

    public static void clearCache() {
        modifiedComponentsCache.clear();
    }

    public static int getCacheSize() {
        return modifiedComponentsCache.size();
    }
}
