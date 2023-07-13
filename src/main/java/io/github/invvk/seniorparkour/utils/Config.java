package io.github.invvk.seniorparkour.utils;

import io.github.invvk.seniorparkour.SeniorParkour;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    @Getter
    private final YamlConfiguration config;
    private final File file;

    public Config(String name, boolean fromResources) {
        this.file = new File(SeniorParkour.inst().getDataFolder(),
                name + ".yml");
        if (!file.exists()) {
            if (fromResources)
                SeniorParkour.inst().saveResource(name + ".yml", false);
            else {
                try {
                    file.createNewFile();
                } catch (IOException ignored) {
                    SeniorParkour.inst().getLogger().severe("failed to create file " + name);
                }
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Config(String name) {
        this(name, false);
    }

    public void save() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
