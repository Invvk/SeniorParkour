package io.github.invvk.seniorparkour.listener;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.event.ParkourPlatePressEvent;
import io.github.invvk.seniorparkour.event.PlateType;
import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class ParkourListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onStartPress(ParkourPlatePressEvent event) {
        if (event.getPlateType() != PlateType.START)
            return;

        var player = event.getPlayer();
        var game = event.getGameData();

        SeniorParkour.inst().getGameManager().start(player, game);
    }

    @EventHandler
    public void onEndPress(ParkourPlatePressEvent event) {
        if (event.getPlateType() != PlateType.END) return;

        var player = event.getPlayer();
        if (SeniorParkour.inst().getGameManager().getParkourPlayer(player.getUniqueId()).isEmpty()) return;

        SeniorParkour.inst().getGameManager().end(player);
    }

    @EventHandler
    public void onCheckpoint(ParkourPlatePressEvent event) {
        if (event.getPlateType() != PlateType.CHECKPOINT) return;
        var player = event.getPlayer();
        var optional = SeniorParkour.inst().getGameManager().getParkourPlayer(player.getUniqueId());
        if (optional.isEmpty()) return;

        var parkourPlayer = optional.get();

        int checkpoint = -1;

        for (var entry: event.getGameData().getCheckpoints().entrySet()) {
            if (Utils.compareLoc(entry.getValue(), event.getLoc())) {
                checkpoint = entry.getKey();
                break;
            }
        }

        if (checkpoint == -1) {
            SeniorParkour.inst().getLogger().severe("Incorrect checkpoint for " + event.getGameData().getName());
            return;
        }

        if (!parkourPlayer.isValidCheckpoint(checkpoint))
            return;

        parkourPlayer.setCheckpoint(event.getLoc());
        Utils.sendCnfMessage(player, MessageProperties.PLAYER_GAME_CHECKPOINT, Map.of("checkpoint", String.valueOf(checkpoint)));
    }

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var optional = SeniorParkour.inst().getGameManager().getParkourPlayer(player.getUniqueId());
        if (optional.isEmpty()) return;
        var parkourPlayer = optional.get();
        if (parkourPlayer.getCheckpoint().v1() == 0
                || parkourPlayer.getCheckpoint().v2() == null)
            return;

        if (player.getFallDistance() > SeniorParkour.inst().getConfig().getInt(ConfigProperties.PARKOUR_MAX_FALL_DISTANCE)) {
            var location = parkourPlayer.getCheckpoint().v2();
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
            location.setYaw(player.getLocation().getYaw());
            location.setPitch(player.getLocation().getPitch());

            player.teleport(location);
            player.setVelocity(new Vector(0, 0 ,0));
        }
    }
}
