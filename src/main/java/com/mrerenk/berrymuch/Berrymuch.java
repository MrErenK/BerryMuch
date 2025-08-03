package com.mrerenk.berrymuch;

import com.mrerenk.berrymuch.command.ConfigReloadCommand;
import com.mrerenk.berrymuch.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Berrymuch implements ModInitializer {

    public static final String MOD_ID = "berry-much";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ModConfig config;

    @Override
    public void onInitialize() {
        // Load configuration
        config = ModConfig.load();

        // Register commands
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {
                ConfigReloadCommand.register(dispatcher);
            }
        );

        LOGGER.info(
            "Berry Much initialized with {} items configured",
            config.items.size()
        );
    }

    public static ModConfig getConfig() {
        return config;
    }

    public static void setConfig(ModConfig newConfig) {
        config = newConfig;
    }
}
