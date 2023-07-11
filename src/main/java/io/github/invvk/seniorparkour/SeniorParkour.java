package io.github.invvk.seniorparkour;

import io.github.invvk.seniorparkour.config.ConfigManager;
import io.github.invvk.seniorparkour.utils.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class SeniorParkour extends JavaPlugin implements Listener {

    private static SeniorParkour instance;

    private ConfigManager cnfManager;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
//        cnfManager = new ConfigManager(this.getDataFolder());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void testHologram(PlayerCommandPreprocessEvent e) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = e.getPlayer();
        Hologram.test(player);
    }

    public ConfigManager getCnfManager() {
        return cnfManager;
    }

    public static SeniorParkour getInstance() {
        if (instance == null)
            throw new RuntimeException("You can't access SeniorParkour instance before the plugin loads");
        return instance;
    }
}
