package io.github.invvk.seniorparkour.utils.gui;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.utils.Utils;
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
        TopPlayerInventoryProvider provider = new TopPlayerInventoryProvider(data,section);

        int size = section.getInt("size", 6);
        var inv = RyseInventory
                .builder()
                .rows(size)
                .provider(provider)
                .disableUpdateTask()
                .build(SeniorParkour.inst());
        inv.open(player);
    }

    public static ItemStack buildItem(ConfigurationSection cnf, String key, Map<String, String> placeholder) {
        Material type = Material.matchMaterial(cnf.getString(key + ".type", "AIR"));
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
