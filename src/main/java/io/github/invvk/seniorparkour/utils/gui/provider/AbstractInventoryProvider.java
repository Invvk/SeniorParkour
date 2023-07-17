package io.github.invvk.seniorparkour.utils.gui.provider;

import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@RequiredArgsConstructor
public abstract class AbstractInventoryProvider implements InventoryProvider {

    protected final ConfigurationSection cnf;

}
