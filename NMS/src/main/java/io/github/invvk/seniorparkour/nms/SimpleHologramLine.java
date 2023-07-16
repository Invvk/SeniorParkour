package io.github.invvk.seniorparkour.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class SimpleHologramLine extends AbstractHologramLine{
    public SimpleHologramLine(Location loc, String text) {
        super(loc, text);
    }

    @Override
    public void spawn(Player player) {
        if (this.id != -1)
            destroy(player);

        World world = loc.getWorld();

        ArmorStand eas = world.spawn(loc, ArmorStand.class);
        eas.setInvisible(true);
        eas.setSmall(true);
        eas.setCustomNameVisible(true);
        eas.setCustomName(this.text);

        this.id = eas.getEntityId();
    }

    @Override
    public void update(Player player, String text) {
        if (id == -1) spawn(player);

        var e = loc.getWorld().
                getEntitiesByClass(ArmorStand.class)
                .stream()
                .filter(x -> x.getEntityId() == this.id)
                .findFirst().orElse(null);
        if (e == null)
            return;

        e.setCustomName(text);
        this.text = text;
    }

    @Override
    public void destroy(Player player) {
        if (id == -1) return;
        var e = loc.getWorld().
                getEntitiesByClass(ArmorStand.class)
                .stream()
                .filter(x -> x.getEntityId() == this.id)
                .findFirst().orElse(null);
        if (e == null) {
            id = -1;
            return;
        }
        e.remove();
        id = -1;
    }

    @Override
    public boolean isPacket() {
        return false;
    }
}
