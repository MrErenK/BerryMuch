package com.mrerenk.berrymuch.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mrerenk.berrymuch.Berrymuch;
import com.mrerenk.berrymuch.config.ModConfig;
import com.mrerenk.berrymuch.util.ComponentCacheManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ConfigReloadCommand {

    public static void register(
        CommandDispatcher<ServerCommandSource> dispatcher
    ) {
        dispatcher.register(
            CommandManager.literal("berrymuch")
                .requires(source -> source.hasPermissionLevel(2)) // Op level 2
                .then(
                    CommandManager.literal("reload").executes(
                        ConfigReloadCommand::reloadConfig
                    )
                )
        );
    }

    private static int reloadConfig(
        CommandContext<ServerCommandSource> context
    ) {
        ServerCommandSource source = context.getSource();

        try {
            int oldCacheSize = ComponentCacheManager.getCacheSize();

            // Reload the config
            ModConfig newConfig = ModConfig.load();

            // Update the static config reference
            Berrymuch.setConfig(newConfig);

            source.sendFeedback(
                () ->
                    Text.literal(
                        "§a[Berry Much] Config reloaded successfully! Modified " +
                        newConfig.items.size() +
                        " items. Cache cleared (" +
                        oldCacheSize +
                        " entries)."
                    ),
                true
            );

            Berrymuch.LOGGER.info(
                "Config reloaded via command by {}",
                source.getName()
            );

            return 1;
        } catch (Exception e) {
            source.sendFeedback(
                () ->
                    Text.literal(
                        "§c[Berry Much] Failed to reload config: " +
                        e.getMessage()
                    ),
                false
            );

            Berrymuch.LOGGER.error(
                "Failed to reload config via command: {}",
                e.getMessage()
            );

            return 0;
        }
    }
}
