package io.github.invvk.seniorparkour.utils.gui.provider;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.gui.GUIConfigManager;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ParkourDataInventoryProvider extends AbstractInventoryProvider {

    private final ParkourGameData data;

    public ParkourDataInventoryProvider(ConfigurationSection cnf, ParkourGameData data) {
        super(cnf);
        this.data = data;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        var pagination = contents.pagination();
        pagination.iterator(SlotIterator.builder()
                .startPosition(cnf.getInt("checkpoints.starting-slot")).build());
        pagination.setItemsPerPage(cnf.getInt("checkpoints.items-per-page"));

        var checkpoints = data.getCheckpoints();
        for (var checkpoint: checkpoints.entrySet()) {
            ItemStack stack = GUIConfigManager
                    .buildItem(cnf, "checkpoints", buildPlaceholders(String.valueOf(checkpoint.getKey()),
                            checkpoint.getValue()));
            pagination.addItem(IntelligentItem.of(stack, event -> player.teleport(checkpoint.getValue())));
        }

        var start = GUIConfigManager
                .buildItem(cnf, "start", buildPlaceholders("start", data.getStart()));
        contents.set(cnf.getInt("start.slot"),
                IntelligentItem.of(start, event -> player.teleport(data.getStart())));

        var end = GUIConfigManager
                .buildItem(cnf, "end", buildPlaceholders("end", data.getEnd()));
        contents.set(cnf.getInt("end.slot"),
                IntelligentItem.of(end, event -> player.teleport(data.getEnd())));

        var top = GUIConfigManager
                .buildItem(cnf, "top", buildPlaceholders("top", data.getTop()));
        contents.set(cnf.getInt("top.slot"),
                IntelligentItem.of(top, event -> player.teleport(data.getTop())));
    }

    private Map<String, String> buildPlaceholders(String name, Location location) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("id", name);
        placeholders.put("location", String.format("%d, %d, %d", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        return placeholders;

    }
}
