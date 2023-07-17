package io.github.invvk.seniorparkour.listener;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();
        final String name = event.getName();

        SeniorParkour.inst().getUserManager().createUser(uuid, name);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        SeniorParkour.inst().getHologramManager().spawn(player);
        for (var data : SeniorParkour.inst().getGameManager().getParkours().values()) {
            if (data.getTop() == null)
                continue;
            Utils.updateTopHologram(player, data);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SeniorParkour.inst().getHologramManager().destroy(event.getPlayer());
        SeniorParkour.inst().getUserManager().invalidate(event.getPlayer().getUniqueId());
    }

}
