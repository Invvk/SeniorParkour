package io.github.invvk.seniorparkour.nms;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramV1_16 implements IHologram {
    @Override
    public int spawn(Player player, Location loc, String message) {
        var cPlayer = ((CraftPlayer) player);
        World world = cPlayer.getHandle().getWorld();
        
        EntityArmorStand eas = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        eas.setInvisible(true);
        eas.setSmall(true);
        eas.setCustomNameVisible(true);
        eas.setCustomName(new ChatComponentText(message));
        
        PacketPlayOutSpawnEntity pose = new PacketPlayOutSpawnEntity(eas);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(eas.getId(), eas.getDataWatcher(), true);
        cPlayer.getHandle().playerConnection.sendPacket(pose);
        cPlayer.getHandle().playerConnection.sendPacket(metadata);
        return eas.getId();
    }

    @Override
    public void destroy(Player player, int... ids) {
        PacketPlayOutEntityDestroy poed = new PacketPlayOutEntityDestroy(ids);
        var cPlayer = ((CraftPlayer)player);
        cPlayer.getHandle().playerConnection.sendPacket(poed);
    }
}
