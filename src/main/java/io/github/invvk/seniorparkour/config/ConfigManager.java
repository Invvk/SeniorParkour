package io.github.invvk.seniorparkour.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import io.github.invvk.seniorparkour.config.holder.ConfigHolder;
import io.github.invvk.seniorparkour.config.holder.MessageHolder;
import io.github.invvk.seniorparkour.config.holder.ParkourHolder;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;

public class ConfigManager {

    @Getter private final SettingsManager config, message, parkour;

    private final File dataFolder;

    public ConfigManager(@NonNull File dataFolder) {
        this.dataFolder = dataFolder;
        config = construct("config", ConfigHolder.class);
        message = construct("messages", MessageHolder.class);
        parkour = construct("parkour", ParkourHolder.class);
    }

    private SettingsManager construct(String fileName, Class<? extends SettingsHolder> holder) {
        return SettingsManagerBuilder.withYamlFile(new File(dataFolder, fileName + ".yml"))
                .useDefaultMigrationService()
                .configurationData(holder)
                .create();
    }
}
