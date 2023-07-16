package io.github.invvk.seniorparkour.nms;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramLineV1_16 extends AbstractHologramLine {

    public HologramLineV1_16(Location loc, String text) {
        super(loc, text);
    }

    @Override
    public void spawn(Player player) {
        if (this.id != -1)
            destroy(player);

        var cPlayer = ((CraftPlayer) player);
        World world = cPlayer.getHandle().getWorld();

        EntityArmorStand eas = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        eas.setInvisible(true);
        eas.setSmall(true);
        eas.setCustomNameVisible(true);
        eas.setCustomName(new ChatComponentText(this.text));

        PacketPlayOutSpawnEntity pose = new PacketPlayOutSpawnEntity(eas);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
        cPlayer.getHandle().playerConnection.sendPacket(pose);
        cPlayer.getHandle().playerConnection.sendPacket(metadata);
        this.id = eas.getId();
    }

    @Override
    public void update(Player player, String text) {
        if (id == -1) spawn(player);
        var cPlayer = ((CraftPlayer) player);

        Entity e = cPlayer.getHandle().getWorld().getEntity(this.id);
        if (e == null)
            return;

        if (e.getEntityType() != EntityTypes.ARMOR_STAND)
            return;

        EntityArmorStand eas = (EntityArmorStand) e;
        eas.setCustomName(new ChatComponentText(text));

        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
        cPlayer.getHandle().playerConnection.sendPacket(metadata);
        this.text = text;
    }

    @Override
    public void destroy(Player player) {
        if (id == -1) return;
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(id);
        var cPlayer = ((CraftPlayer)player);
        cPlayer.getHandle().playerConnection.sendPacket(destroy);
        id = -1;
    }

}
