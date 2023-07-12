package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramV1_19 implements IHologram {
    @Override
    public void spawnPlayerHologram(Player player, String message) {
        var craftPlayer = (CraftPlayer) player;
        var world = craftPlayer.getHandle().getLevel();

        Location loc = player.getLocation();
        ArmorStand as = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        as.setInvisible(true);
        as.setSmall(true);
        as.setCustomNameVisible(true);
        as.setCustomName(Component.literal("Hello " + player.getName()));

        ClientboundAddEntityPacket cb = new ClientboundAddEntityPacket(as);
        craftPlayer.getHandle().connection.send(cb);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData().getNonDefaultValues());
        craftPlayer.getHandle().connection.send(metadata);
    }

    @Override
    public void destroyPlayerHologram(Player player) {

    }
}
