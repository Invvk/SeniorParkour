package io.github.invvk.seniorparkour.nms;

import org.bukkit.entity.Player;

public interface IHologram {

    void spawnPlayerHologram(Player player, String message);

    void destroyPlayerHologram(Player player);

}
