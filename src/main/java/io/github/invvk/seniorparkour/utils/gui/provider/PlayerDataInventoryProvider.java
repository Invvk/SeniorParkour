package io.github.invvk.seniorparkour.utils.gui.provider;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.gui.GUIConfigManager;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerDataInventoryProvider extends AbstractInventoryProvider {

    private final Player target;

    public PlayerDataInventoryProvider(ConfigurationSection cnf, Player target) {
        super(cnf);
        this.target = target;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        var pagination = contents.pagination();

        pagination.setItemsPerPage(cnf.getInt("parkours.items-per-page"));
        pagination.iterator(SlotIterator.builder()
                .startPosition(cnf.getInt("parkours.starting-slot")).build());

        var parkours = SeniorParkour.inst().getGameManager().getParkours().values();
        for (var parkour: parkours) {
            ItemStack stack = GUIConfigManager.buildItem(cnf, "parkours", Utils.constructPlaceholders(target, parkour));
            pagination.addItem(stack);
        }
    }
}
