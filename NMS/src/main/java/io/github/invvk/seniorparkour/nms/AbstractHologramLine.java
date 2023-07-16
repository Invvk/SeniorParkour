package io.github.invvk.seniorparkour.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AbstractHologramLine {

    private final UUID uuid = UUID.randomUUID();
    protected final Location loc;
    protected int id = -1;
    protected String text;

    public AbstractHologramLine(Location loc, String text) {
        this.loc = loc;
        this.text = text;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public abstract void spawn(Player player);

    public abstract void update(Player player, String text);
    public abstract void destroy(Player player);

    public boolean isPacket() {
        return true;
    }

}
