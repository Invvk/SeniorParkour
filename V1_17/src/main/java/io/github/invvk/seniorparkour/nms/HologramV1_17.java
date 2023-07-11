package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramV1_17 implements IHologram {
    @Override
    public void spawnPlayerHologram(Player player, String message) {
        var craftPlayer = (CraftPlayer) player;
        Level world = craftPlayer.getHandle().getLevel();

        Location loc = player.getLocation();

        ArmorStand armorStand = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        armorStand.setNoGravity(true);
        armorStand.setCustomName(new TextComponent("Hello There UwU " + player.getName()));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        ClientboundAddEntityPacket sp = new ClientboundAddEntityPacket(armorStand);
        craftPlayer.getHandle().networkManager.send(sp);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true);
        craftPlayer.getHandle().networkManager.send(metadata);
    }

    @Override
    public void destroyPlayerHologram(Player player) {

    }
}
