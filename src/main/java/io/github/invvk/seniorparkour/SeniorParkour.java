package io.github.invvk.seniorparkour;

import io.github.invvk.seniorparkour.commands.ParkourMainCommand;
import io.github.invvk.seniorparkour.config.ConfigManager;
import io.github.invvk.seniorparkour.database.StorageManager;
import io.github.invvk.seniorparkour.database.UserManager;
import io.github.invvk.seniorparkour.game.GameManager;
import io.github.invvk.seniorparkour.listener.BlockListener;
import io.github.invvk.seniorparkour.listener.ConnectionListener;
import io.github.invvk.seniorparkour.listener.ParkourListener;
import io.github.invvk.seniorparkour.utils.gui.GUIConfigManager;
import io.github.invvk.seniorparkour.utils.holograms.HologramManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SeniorParkour extends JavaPlugin implements Listener {

    private static SeniorParkour instance;

    @Getter private ConfigManager cnfManager;
    @Getter private GameManager gameManager;

    @Getter private StorageManager storageManager;
    @Getter private UserManager userManager;
    @Getter private GUIConfigManager guiConfigManager;

    @Getter private HologramManager hologramManager;
    @Getter private final InventoryManager inventoryManager = new InventoryManager(this);

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        instance = this;
        cnfManager = new ConfigManager();
        hologramManager = new HologramManager();
        storageManager = new StorageManager();
        userManager = new UserManager();
        gameManager = new GameManager();
        guiConfigManager = new GUIConfigManager();

        inventoryManager.invoke();

        getCommand("parkour").setExecutor(new ParkourMainCommand());
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new ParkourListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
        if (hologramManager != null)
            hologramManager.destroyAll();
    }

    public static SeniorParkour inst() {
        return instance;
    }
}
