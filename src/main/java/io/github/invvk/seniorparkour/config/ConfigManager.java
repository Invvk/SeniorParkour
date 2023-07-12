package io.github.invvk.seniorparkour.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.config.holder.ParkourProperties;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;

public class ConfigManager {

    @Getter private final SettingsManager config, message, parkour;

    private final File dataFolder;

    public ConfigManager(@NonNull File dataFolder) {
        this.dataFolder = dataFolder;
        config = construct("config", ConfigProperties.class);
        message = construct("messages", MessageProperties.class);
        parkour = construct("parkour", ParkourProperties.class);
    }

    private SettingsManager construct(String fileName, Class<? extends SettingsHolder> holder) {
        return SettingsManagerBuilder.withYamlFile(new File(dataFolder, fileName + ".yml"))
                .useDefaultMigrationService()
                .configurationData(holder)
                .create();
    }
}
