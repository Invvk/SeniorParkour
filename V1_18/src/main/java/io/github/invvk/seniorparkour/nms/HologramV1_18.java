package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramV1_18 implements IHologram{
    @Override
    public void spawnPlayerHologram(Player player, String message) {
        var craftPlayer = ((CraftPlayer)player);
        var world = craftPlayer.getHandle().getLevel();
        Location loc = player.getLocation();
        ArmorStand as = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        as.setInvisible(true);
        as.setSmall(true);
        as.setCustomNameVisible(true);
        as.setCustomName(new TextComponent("Hello " + player.getName()));

        ClientboundAddEntityPacket cb = new ClientboundAddEntityPacket(as);
        craftPlayer.getHandle().networkManager.send(cb);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData(), true);
        craftPlayer.getHandle().networkManager.send(metadata);
    }

    @Override
    public void destroyPlayerHologram(Player player) {

    }
}
