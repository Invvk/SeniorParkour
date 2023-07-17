package io.github.invvk.seniorparkour.utils.gui;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.gui.provider.ParkourDataInventoryProvider;
import io.github.invvk.seniorparkour.utils.gui.provider.PlayerDataInventoryProvider;
import io.github.invvk.seniorparkour.utils.gui.provider.TopPlayerInventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class GUIConfigManager {

    private final YamlConfiguration cnf = SeniorParkour.inst().getCnfManager()
            .getGui().getConfig();

    public void openTopPlayers(Player player, ParkourGameData data) {
        var section = cnf.getConfigurationSection("top-players");
        var itemSection = section.getConfigurationSection("items");
        var provider = new TopPlayerInventoryProvider(data,itemSection);

        int size = section.getInt("size", 6);
        var inv = RyseInventory
                .builder()
                .title(Utils.hex(section.getString("title", "Open Inv")))
                .rows(size)
                .provider(provider)
                .disableUpdateTask()
                .build(SeniorParkour.inst());
        inv.open(player);
    }

    public void openPlayerData(Player player, Player target) {
        var section = cnf.getConfigurationSection("player-stats");
        var itemSection = section.getConfigurationSection("items");
        var provider = new PlayerDataInventoryProvider(itemSection, target);

        int size = section.getInt("size", 6);
        var inv = RyseInventory
                .builder()
                .title(Utils.hex(section.getString("title", "Open Inv")))
                .rows(size)
                .provider(provider)
                .disableUpdateTask()
                .build(SeniorParkour.inst());
        inv.open(player);
    }

    public void openParkourInfo(Player player, ParkourGameData data) {
        var section = cnf.getConfigurationSection("parkour-info");
        var itemSection = section.getConfigurationSection("items");
        var provider = new ParkourDataInventoryProvider(itemSection, data);

        int size = section.getInt("size", 6);
        var inv = RyseInventory
                .builder()
                .title(Utils.hex(section.getString("title", "Open Inv")))
                .rows(size)
                .provider(provider)
                .disableUpdateTask()
                .build(SeniorParkour.inst());
        inv.open(player);
    }

    public static ItemStack buildItem(ConfigurationSection cnf, String key, Map<String, String> placeholder) {
        Material type = Material.matchMaterial(cnf.getString(key + ".type", key.equals("player-head") ? "PLAYER_HEAD" : "AIR"));
        if (type == null || type == Material.AIR) return null;
        String displayName = cnf.getString(key + ".displayName");
        List<String> lore = cnf.getStringList(key + ".lore");

        ItemStack stack = new ItemStack(type);
        var meta = stack.getItemMeta();
        if (meta != null) {
            if (displayName != null && !displayName.isEmpty()) {
                meta.setDisplayName(Utils.replace(displayName, placeholder));
            }
            if (!lore.isEmpty()) {
                meta.setLore(lore.stream().map(x -> Utils.replace(x, placeholder))
                        .toList());
            }
        }
        stack.setItemMeta(meta);
        return stack;
    }

}
