package io.github.invvk.seniorparkour.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IHologram {

    /**
     * Spawn hologram that is specific per player
     * @param player player to show hologram
     * @param message message for the player
     * @return entity id
     */
    int spawn(Player player, Location loc, String message);


    /**
     * Destroy a hologram
     * @param id hologram id
     */
    void destroy(Player player, int... ids);

}
