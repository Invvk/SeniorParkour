package io.github.invvk.seniorparkour;

import io.github.invvk.seniorparkour.commands.ParkourMainCommand;
import io.github.invvk.seniorparkour.config.ConfigManager;
import io.github.invvk.seniorparkour.database.StorageManager;
import io.github.invvk.seniorparkour.game.GameManager;
import io.github.invvk.seniorparkour.listener.BlockListener;
import io.github.invvk.seniorparkour.listener.ParkourListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SeniorParkour extends JavaPlugin implements Listener {

    private static SeniorParkour instance;

    @Getter private ConfigManager cnfManager;
    @Getter private GameManager gameManager;

    @Getter private StorageManager storageManager;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        instance = this;
        cnfManager = new ConfigManager();
        gameManager = new GameManager();

        getCommand("parkour").setExecutor(new ParkourMainCommand());
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new ParkourListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static SeniorParkour inst() {
        return instance;
    }
}
