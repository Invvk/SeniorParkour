package io.github.invvk.seniorparkour;

import io.github.invvk.seniorparkour.commands.ParkourMainCommand;
import io.github.invvk.seniorparkour.config.ConfigManager;
import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SeniorParkour extends JavaPlugin implements Listener {

    private static SeniorParkour instance;

    private ConfigManager cnfManager;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        instance = this;
        cnfManager = new ConfigManager(this.getDataFolder());
        getCommand("parkour").setExecutor(new ParkourMainCommand());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ConfigManager getCnfManager() {
        return cnfManager;
    }

    public static SeniorParkour getInstance() {
        if (instance == null)
            throw new RuntimeException("You can't access SeniorParkour instance before the plugin loads");
        return instance;
    }

    @EventHandler
    public void breakblock(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
           for (var parkour: Utils.getParkourConfigMap().getParkours().values()) {
               if (parkour.isParkourPlate(event.getBlock().getLocation())) {
                   event.setCancelled(true);
                   break;
               }
           }
        }
    }
}
