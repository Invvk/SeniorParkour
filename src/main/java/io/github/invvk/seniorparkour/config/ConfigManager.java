package io.github.invvk.seniorparkour.config;

import io.github.invvk.seniorparkour.utils.Config;
import lombok.Getter;

public class ConfigManager {

    @Getter private final Config config, messages, parkour, gui;

    public ConfigManager() {
        this.config = new Config("config", true);
        this.messages = new Config("messages", true);
        this.parkour = new Config("parkour");
        this.gui = new Config("gui", true);
    }

}
