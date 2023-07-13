package io.github.invvk.seniorparkour.listener;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.event.ParkourPlatePressEvent;
import io.github.invvk.seniorparkour.event.PlateType;
import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            for (var parkour: SeniorParkour.inst().getGameManager().getParkours().values()) {
                if (parkour.isParkourPlate(event.getBlock().getLocation()) != -1) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }


    @EventHandler
    public void onPlateClick(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (event.getAction().equals(Action.PHYSICAL)
                && (block != null &&
                block.getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
            var loc = block.getLocation();
            var player = event.getPlayer();
            for (var parkour: SeniorParkour.inst().getGameManager().getParkours().values()) {
                int result = parkour.isParkourPlate(loc);
                if (result != -1) {
                    event.setCancelled(true);
                    ParkourPlatePressEvent pressEvent = new ParkourPlatePressEvent(player, loc, PlateType.fromInt(result), parkour);
                    Bukkit.getPluginManager().callEvent(pressEvent);

                    break;
                }
            }
        }
    }

}
