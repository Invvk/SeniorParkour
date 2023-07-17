package io.github.invvk.seniorparkour.utils.gui.provider;

import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.gui.GUIConfigManager;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;

public class TopPlayerInventoryProvider extends AbstractInventoryProvider {

    private final ParkourGameData data;

    public TopPlayerInventoryProvider(ParkourGameData data, ConfigurationSection cnf) {
        super(cnf);
        this.data = data;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(9);

        int starting_position = cnf.getInt("player-head.starting-slot");
        pagination.iterator(SlotIterator.builder().startPosition(starting_position).build());
        for (var topPlayer: data.getTopPlayers()) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("position", String.valueOf(topPlayer.position()));
            placeholders.put("name", topPlayer.name());
            placeholders.put("time", Utils.formatTime(topPlayer.time()));

            ItemStack stack = GUIConfigManager.buildItem(cnf, "player-head",
                    placeholders);

            var skull = (SkullMeta) stack.getItemMeta();
            skull.setOwner(topPlayer.name());
            stack.setItemMeta(skull);

            pagination.addItem(stack);
        }

        ItemStack next = GUIConfigManager.buildItem(cnf, "next", Map.of());
        ItemStack previous = GUIConfigManager.buildItem(cnf, "previous", Map.of());

        int nextIndex = cnf.getInt("next.slot");
        int previousIndex = cnf.getInt("previous.slot");

        contents.set(nextIndex, IntelligentItem
                .of(next, e -> {
                            if (pagination.isLast()) {
                                Utils.sendCnfMessage(player, MessageProperties.GUI_LAST_PAGE);
                                return;
                            }
                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.next().page());
                        }));


        contents.set(previousIndex, IntelligentItem.of(previous, event -> {
            if (pagination.isFirst()) {
                Utils.sendCnfMessage(player, MessageProperties.GUI_FIRST_PAGE);
                return;
            }
            RyseInventory currentInventory = pagination.inventory();
            currentInventory.open(player, pagination.next().page());;
        }));
    }
}
