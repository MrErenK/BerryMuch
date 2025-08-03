package com.mrerenk.berrymuch.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mrerenk.berrymuch.Berrymuch;
import com.mrerenk.berrymuch.util.ComponentCacheManager;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private static final String CONFIG_FILE_NAME = "berry-much.json";

    public List<ItemConfig> items = new ArrayList<>();

    // Cache for quick lookup during mixin operations
    private transient Map<String, ItemConfig> itemCache;

    public static class ItemConfig {

        public String itemId;
        public int nutrition;
        public float saturation;

        // Type removed - mixins handle all items

        public ItemConfig() {}

        public ItemConfig(String itemId, int nutrition, float saturation) {
            this.itemId = itemId;
            this.nutrition = nutrition;
            this.saturation = saturation;
        }
    }

    public static ModConfig load() {
        Path configPath = getConfigPath();

        if (!Files.exists(configPath)) {
            ModConfig defaultConfig = loadDefaultFromResources();
            defaultConfig.save();
            Berrymuch.LOGGER.info("Created default config file");
            return defaultConfig;
        }

        try {
            String configContent = Files.readString(configPath);
            Type configType = new TypeToken<ModConfig>() {}.getType();
            ModConfig config = GSON.fromJson(configContent, configType);

            if (config == null) {
                Berrymuch.LOGGER.warn(
                    "Config file is empty or invalid, using default config"
                );
                config = loadDefaultFromResources();
                config.save();
            }

            // Build cache for quick lookup
            config.buildCache();

            // Clear component cache when config is reloaded
            ComponentCacheManager.clearCache();

            return config;
        } catch (IOException e) {
            Berrymuch.LOGGER.error(
                "Failed to load config file, using default config: {}",
                e.getMessage()
            );
            ModConfig defaultConfig = loadDefaultFromResources();
            defaultConfig.save();
            // Clear cache for default config too
            ComponentCacheManager.clearCache();
            return defaultConfig;
        }
    }

    public void save() {
        try {
            Path configPath = getConfigPath();
            Files.createDirectories(configPath.getParent());
            String json = GSON.toJson(this);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            Berrymuch.LOGGER.error(
                "Failed to save config file: {}",
                e.getMessage()
            );
        }
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance()
            .getConfigDir()
            .resolve(CONFIG_FILE_NAME);
    }

    private static ModConfig loadDefaultFromResources() {
        try {
            InputStream inputStream = ModConfig.class.getResourceAsStream(
                "/default-config.json"
            );
            if (inputStream == null) {
                Berrymuch.LOGGER.error(
                    "Could not find default-config.json in resources"
                );
                return createFallbackConfig();
            }

            String jsonContent = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
            );
            Type configType = new TypeToken<ModConfig>() {}.getType();
            ModConfig config = GSON.fromJson(jsonContent, configType);

            if (config != null) {
                config.buildCache();
                return config;
            } else {
                Berrymuch.LOGGER.warn(
                    "Default config JSON was invalid, using fallback"
                );
                return createFallbackConfig();
            }
        } catch (IOException e) {
            Berrymuch.LOGGER.error(
                "Failed to load default config from resources: {}",
                e.getMessage()
            );
            return createFallbackConfig();
        }
    }

    private static ModConfig createFallbackConfig() {
        ModConfig config = new ModConfig();
        // Minimal fallback config
        config.items.add(new ItemConfig("minecraft:sweet_berries", 6, 1.2f));
        config.items.add(new ItemConfig("minecraft:glow_berries", 4, 0.8f));
        config.items.add(new ItemConfig("minecraft:melon_slice", 2, 2.4f));
        return config;
    }

    private void buildCache() {
        itemCache = new HashMap<>();
        for (ItemConfig item : items) {
            itemCache.put(item.itemId, item);
        }
    }

    // Helper methods for filtering items if needed
    public List<ItemConfig> getMinecraftItems() {
        return items
            .stream()
            .filter(item -> item.itemId.startsWith("minecraft:"))
            .toList();
    }

    public List<ItemConfig> getModdedItems() {
        return items
            .stream()
            .filter(item -> !item.itemId.startsWith("minecraft:"))
            .toList();
    }

    public ItemConfig getItemConfig(String itemId) {
        if (itemCache == null) {
            buildCache();
        }
        return itemCache.get(itemId);
    }

    public boolean hasItem(String itemId) {
        return getItemConfig(itemId) != null;
    }
}
